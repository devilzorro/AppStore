package com.i5cnc.appstore.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AppListResponseModel {
    /**
     * status : 0
     * message :
     * content : {"appList":[{"id":0,"type":0,"label":"","icon":"","url":"","file":"","package":"","activity":"","versionCode":0,"versionName":"","info":""}]}
     */

    private int status;
    private String message;
    private ContentBean content;



    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ContentBean getContent() {
        return content;
    }

    public void setContent(ContentBean content) {
        this.content = content;
    }

    public static class ContentBean {
        private List<AppListBean> appList;

        public List<AppListBean> getAppList() {
            return appList;
        }

        public void setAppList(List<AppListBean> appList) {
            this.appList = appList;
        }

        public static class AppListBean {
            /**
             * id : 0
             * type : 0
             * label :
             * icon :
             * url :
             * file :
             * package :
             * activity :
             * versionCode : 0
             * versionName :
             * info :
             */

            private int id;
            private int type;
            private String label;
            private String icon;
            private String url;
            private String file;
            @SerializedName("package")
            private String packageX;
            private String activity;
            private int versionCode;
            private String versionName;
            private String info;

            public AppListBean() {
            }

            public AppListBean(int id, int type, String label, String icon, String url, String file, String packageX, String activity, int versionCode, String versionName, String info) {
                this.id = id;
                this.type = type;
                this.label = label;
                this.icon = icon;
                this.url = url;
                this.file = file;
                this.packageX = packageX;
                this.activity = activity;
                this.versionCode = versionCode;
                this.versionName = versionName;
                this.info = info;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }

            public String getLabel() {
                return label;
            }

            public void setLabel(String label) {
                this.label = label;
            }

            public String getIcon() {
                return icon;
            }

            public void setIcon(String icon) {
                this.icon = icon;
            }

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }

            public String getFile() {
                return file;
            }

            public void setFile(String file) {
                this.file = file;
            }

            public String getPackageX() {
                return packageX;
            }

            public void setPackageX(String packageX) {
                this.packageX = packageX;
            }

            public String getActivity() {
                return activity;
            }

            public void setActivity(String activity) {
                this.activity = activity;
            }

            public int getVersionCode() {
                return versionCode;
            }

            public void setVersionCode(int versionCode) {
                this.versionCode = versionCode;
            }

            public String getVersionName() {
                return versionName;
            }

            public void setVersionName(String versionName) {
                this.versionName = versionName;
            }

            public String getInfo() {
                return info;
            }

            public void setInfo(String info) {
                this.info = info;
            }
        }
    }
    //{"status":0,"message":"","content":{"appList":[{"id":0,"type":0,"label":"","icon":"","url":"","file":"","package":"","activity":"","versionCode":0,"versionName":"","info":""}]}}


}
