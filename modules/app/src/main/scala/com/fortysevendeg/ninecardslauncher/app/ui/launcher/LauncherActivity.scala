package com.fortysevendeg.ninecardslauncher.app.ui.launcher

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.fortysevendeg.ninecardslauncher.app.commons.ContextSupportProvider
import com.fortysevendeg.ninecardslauncher.app.di.Injector
import com.fortysevendeg.ninecardslauncher.app.ui.commons.ActivityResult._
import com.fortysevendeg.ninecardslauncher.app.ui.commons.AppUtils._
import com.fortysevendeg.ninecardslauncher.app.ui.commons.TasksOps._
import com.fortysevendeg.ninecardslauncher.app.ui.commons._
import com.fortysevendeg.ninecardslauncher.app.ui.drawer.{ContactsMenuOption, AppsMenuOption, DrawerComposer}
import com.fortysevendeg.ninecardslauncher.app.ui.wizard.WizardActivity
import com.fortysevendeg.ninecardslauncher.process.device.AllContacts
import com.fortysevendeg.ninecardslauncher.process.device.models.{App, Contact}
import com.fortysevendeg.ninecardslauncher.process.theme.models.NineCardsTheme
import com.fortysevendeg.ninecardslauncher2.{R, TypedFindView}
import macroid.FullDsl._
import macroid.{Contexts, Ui}
import rapture.core.Answer

import scalaz.concurrent.Task

class LauncherActivity
  extends AppCompatActivity
  with Contexts[AppCompatActivity]
  with ContextSupportProvider
  with TypedFindView
  with LauncherComposer
  with DrawerComposer
  with SystemBarsTint
  with NineCardIntentConversions
  with LauncherExecutor {

  implicit lazy val di: Injector = new Injector

  implicit lazy val uiContext: UiContext[Activity] = ActivityUiContext(this)

  implicit lazy val theme: NineCardsTheme = di.themeProcess.getSelectedTheme.run.run match {
    case Answer(t) => t
    case _ => getDefaultTheme
  }

  val playStorePackage = "com.android.vending"

  override def onCreate(bundle: Bundle) = {
    super.onCreate(bundle)
    Task.fork(di.userProcess.register.run).resolveAsync()
    setContentView(R.layout.launcher_activity)
    runUi(initUi ~ initDrawerUi(
      launchStore = () => launchApp(playStorePackage),
      launchDial = () => launchDial(None),
      onAppMenuClickListener = loadApps,
      onContactMenuClickListener = loadContacts))
    initAllSystemBarsTint
    generateCollections()
  }

  override def onActivityResult(requestCode: Int, resultCode: Int, data: Intent): Unit = {
    super.onActivityResult(requestCode, resultCode, data)
    (requestCode, resultCode) match {
      case (request, result) if result == Activity.RESULT_OK && request == wizard =>
        generateCollections()
      case _ =>
    }
  }

  override def onBackPressed(): Unit = if (isMenuVisible) {
    runUi(closeMenu())
  } else if (fabMenuOpened) {
    runUi(swapFabButton())
  } else if (isDrawerVisible) {
    runUi(revealOutDrawer)
  }

  private[this] def generateCollections() = Task.fork(di.collectionProcess.getCollections.run).resolveAsyncUi(
    onResult = {
      // Check if there are collections in DB, if there aren't we go to wizard
      case Nil => goToWizard()
      case collections =>
        getUserInfo()
        createCollections(collections)
    },
    onException = (ex: Throwable) => goToWizard(),
    onPreTask = () => showLoading
  )

  private[this] def getUserInfo() = Task.fork(di.userConfigProcess.getUserInfo.run).resolveAsyncUi(
    onResult = userInfoMenu
  )

  private[this] def goToWizard(): Ui[_] = Ui {
    val wizardIntent = new Intent(LauncherActivity.this, classOf[WizardActivity])
    startActivityForResult(wizardIntent, wizard)
  }

  private[this] def loadApps(appsMenuOption: AppsMenuOption): Unit =
    // TODO - Take into account the `appsMenuOption` param
    Task.fork(di.deviceProcess.getSavedApps.run).resolveAsyncUi(
      onPreTask = () => showDrawerLoading,
      onResult = (apps: Seq[App]) => addApps(apps, (app: App) => {
        execute(toNineCardIntent(app))
      })
    )

  private[this] def loadContacts(contactsMenuOption: ContactsMenuOption): Unit =
    // TODO - Take into account the `contactsMenuOption` param
    Task.fork(di.deviceProcess.getContacts(filter = AllContacts).run).resolveAsyncUi(
      onPreTask = () => showDrawerLoading,
      onResult = (contacts: Seq[Contact]) => addContacts(contacts, (contact: Contact) => {
        execute(contact)
      })
    )

}
