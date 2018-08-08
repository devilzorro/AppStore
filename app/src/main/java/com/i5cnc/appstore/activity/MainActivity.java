package com.i5cnc.appstore.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.i5cnc.appstore.R;
import com.i5cnc.appstore.adapter.AppListAdapter;
import com.i5cnc.appstore.global.Constant;
import com.i5cnc.appstore.model.AppListResponseModel;
import com.i5cnc.appstore.util.CommonUtil;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.FileCallback;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Progress;
import com.lzy.okgo.model.Response;
import com.qihoo360.replugin.RePlugin;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    private String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private List<String> permissionList = new ArrayList<>();

    @BindView(R.id.rv_internal)
    RecyclerView mRvInternal;
    @BindView(R.id.rv_external)
    RecyclerView mRvExternal;

    private List<AppListResponseModel.ContentBean.AppListBean> internalDatas = new ArrayList<>();
    private List<AppListResponseModel.ContentBean.AppListBean> externalDatas = new ArrayList<>();

    private AppListAdapter internalAdapter;
    private AppListAdapter externalAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        checkPermissions();
        initRecyclerView();
        refresh();
    }

    private void checkPermissions() {
        permissionList.clear();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                permissionList.add(permission);
            }
        }
        if (permissionList.size() == 0) {

        } else {
            ActivityCompat.requestPermissions(this, permissionList.toArray(new String[permissionList.size()]), 101);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 101) {
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, permissions[i])) {
                        checkPermissions();
                    } else {
                        finish();
                    }
                    return;
                }
            }
        }
    }

    private void refresh() {
        internalDatas.clear();
        internalDatas.add(new AppListResponseModel.ContentBean.AppListBean(0, 0, "InternalApp", "https://github.com/Qihoo360/RePlugin/wiki/img/apps/mobilesafe.png", "", "", "com.i5cnc.internalapp", "com.i5cnc.internalapp.MainActivity", 1, "1.0", ""));
        internalDatas.add(new AppListResponseModel.ContentBean.AppListBean(3, 0, "Demo1", "https://github.com/Qihoo360/RePlugin/wiki/img/apps/mobilesafe.png", "", "", "com.qihoo360.replugin.sample.demo1", "com.qihoo360.replugin.sample.demo1.MainActivity", 1, "1.0", ""));
        internalAdapter.notifyDataSetChanged();

        OkGo.<String>get(Constant.host + "/AppStore/applist.json").execute(new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                AppListResponseModel appListResponseModel = CommonUtil.gson.fromJson(response.body(), AppListResponseModel.class);
                if (appListResponseModel.getStatus() == 0) {
                    externalDatas.clear();
                    externalDatas.addAll(appListResponseModel.getContent().getAppList());
                    externalAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    private void initRecyclerView() {
        mRvInternal.setLayoutManager(new GridLayoutManager(this, 5));
        mRvExternal.setLayoutManager(new GridLayoutManager(this, 5));
        internalAdapter = new AppListAdapter(internalDatas);
        internalAdapter.setOnItemClickListener(new AppListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                AppListResponseModel.ContentBean.AppListBean appListBean = internalDatas.get(position);
                RePlugin.startActivity(MainActivity.this, RePlugin.createIntent(appListBean.getPackageX(), appListBean.getActivity()));
            }
        });
        externalAdapter = new AppListAdapter(externalDatas);
        externalAdapter.setOnItemClickListener(new AppListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                final AppListResponseModel.ContentBean.AppListBean appListBean = externalDatas.get(position);
                if (RePlugin.isPluginInstalled(appListBean.getPackageX())) {
                    RePlugin.startActivity(MainActivity.this, RePlugin.createIntent(appListBean.getPackageX(), appListBean.getActivity()));
                } else {
                    final ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
                    progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                    progressDialog.show();
                    OkGo.<File>get(Constant.host + appListBean.getUrl()).execute(new FileCallback() {
                        @Override
                        public void onSuccess(Response<File> response) {
                            progressDialog.dismiss();
                            RePlugin.install(response.body().getAbsolutePath());
                            RePlugin.startActivity(MainActivity.this, RePlugin.createIntent(appListBean.getPackageX(), appListBean.getActivity()));
                        }

                        @Override
                        public void downloadProgress(Progress progress) {
                            progressDialog.setProgress((int) (progress.fraction * 100));
                        }
                    });
                }
            }
        });
        mRvInternal.setAdapter(internalAdapter);
        mRvExternal.setAdapter(externalAdapter);
    }
}
