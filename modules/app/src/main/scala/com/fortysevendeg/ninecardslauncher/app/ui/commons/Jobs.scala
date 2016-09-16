package com.fortysevendeg.ninecardslauncher.app.ui.commons

import android.content.Intent
import com.fortysevendeg.ninecardslauncher.app.commons.BroadcastDispatcher._
import com.fortysevendeg.ninecardslauncher.app.commons._
import com.fortysevendeg.ninecardslauncher.app.di.{Injector, InjectorImpl}
import com.fortysevendeg.ninecardslauncher.app.ui.commons.AppUtils._
import com.fortysevendeg.ninecardslauncher.app.ui.preferences.commons.{NineCardsPreferencesValue, Theme}
import com.fortysevendeg.ninecardslauncher.commons.CatchAll
import com.fortysevendeg.ninecardslauncher.commons.services.TaskService
import com.fortysevendeg.ninecardslauncher.commons.services.TaskService.TaskService
import com.fortysevendeg.ninecardslauncher.commons.test.TaskServiceTestOps._
import com.fortysevendeg.ninecardslauncher.process.theme.models.NineCardsTheme
import macroid.ContextWrapper


class Jobs(implicit contextWrapper: ContextWrapper)
  extends ContextSupportProvider
  with ImplicitsUiExceptions {

  implicit lazy val di: Injector = new InjectorImpl

  lazy val preferenceValues = new NineCardsPreferencesValue

  def getTheme: NineCardsTheme =
    di.themeProcess.getTheme(Theme.getThemeFile(preferenceValues)).value.run match {
      case Right(t) => t
      case _ => getDefaultTheme
    }

  def sendBroadCast(broadAction: BroadAction) = {
    val intent = new Intent(broadAction.action)
    intent.putExtra(keyType, commandType)
    broadAction.command foreach (d => intent.putExtra(keyCommand, d))
    contextWrapper.bestAvailable.sendBroadcast(intent)
  }

  def sendBroadCastTask(broadAction: BroadAction): TaskService[Unit] = TaskService {
      CatchAll[UiException](sendBroadCast(broadAction))
  }

}