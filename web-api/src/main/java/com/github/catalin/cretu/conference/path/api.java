package com.github.catalin.cretu.conference.path;

@SuppressWarnings({ "squid:S1214", "squid:S00115" })
public interface api {

    String versionedApi = "/api/v1";

    String Events = versionedApi + "/events";

    String Togglz = versionedApi + "/togglz";

    interface togglz {
        String Enable = Togglz + "/enable/";

        static String enableByFeature(String featureName) {
            return Enable + featureName;
        }

        interface enable {
            String byFeatureName = Enable + "{" + PathVars.featureName + "}";
        }

        String Disable = Togglz + "/disable/";

        interface disable {
            String byFeatureName = Disable + "{" + PathVars.featureName + "}";
        }
    }

    interface PathVars {
        String featureName = "featureName";
    }
}