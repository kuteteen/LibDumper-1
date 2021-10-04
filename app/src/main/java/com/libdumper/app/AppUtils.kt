package com.libdumper.app

import android.content.Context
import android.content.pm.ApplicationInfo

import android.content.pm.PackageManager


/**
 * From https://stackoverflow.com/questions/3304685/how-to-get-the-list-of-running-applications
 * Modified by Hao
 */
class AppUtils {
    companion object {
        /**
         * It has some issues such as some apps are not running in fact, but it doesn`t matter. We just need the app list.
         */
        fun getActiveApps(context: Context): ArrayList<AppDetail> {
            val pm: PackageManager = context.getPackageManager()
            val packages = pm.getInstalledApplications(PackageManager.GET_META_DATA)
            var appdetaillist=ArrayList<AppDetail>()
            for (packageInfo in packages) {
                //system apps! get out
                if (!isSTOPPED(packageInfo) && !isSYSTEM(packageInfo)) {
                    appdetaillist.add(
                        AppDetail(
                            appname = getApplicationLabel(
                            context,
                            packageInfo.packageName
                        ),
                            packagename = packageInfo.packageName,
                            applicationInfo = packageInfo
                        )
                    )
                }
            }
            return appdetaillist
        }


        private fun isSTOPPED(pkgInfo: ApplicationInfo): Boolean {
            return pkgInfo.flags and ApplicationInfo.FLAG_STOPPED != 0
        }

        private fun isSYSTEM(pkgInfo: ApplicationInfo): Boolean {
            return pkgInfo.flags and ApplicationInfo.FLAG_SYSTEM != 0
        }
        private fun getApplicationLabel(context: Context, packageName: String): String {
            val packageManager = context.packageManager
            val packages = packageManager.getInstalledApplications(PackageManager.GET_META_DATA)
            var label: String=""
            for (i in packages.indices) {
                val temp = packages[i]
                if (temp.packageName == packageName) label =
                    packageManager.getApplicationLabel(temp).toString()
            }
            return label
        }
    }
}