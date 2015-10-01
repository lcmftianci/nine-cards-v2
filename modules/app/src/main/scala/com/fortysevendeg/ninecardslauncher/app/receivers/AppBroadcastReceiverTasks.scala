package com.fortysevendeg.ninecardslauncher.app.receivers

import com.fortysevendeg.ninecardslauncher.app.di.Injector
import com.fortysevendeg.ninecardslauncher.commons.contexts.ContextSupport

trait AppBroadcastReceiverTasks {

  def addApp(packageName: String)(implicit di: Injector, contextSupport: ContextSupport) =
    di.deviceProcess.saveApp(packageName)

  def deleteApp(packageName: String)(implicit di: Injector, contextSupport: ContextSupport) =
    di.deviceProcess.deleteApp(packageName)

}
