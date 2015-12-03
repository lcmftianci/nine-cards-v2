package com.fortysevendeg.ninecardslauncher.app.ui.launcher.drawer

trait DrawerListeners {
  def loadApps(appsMenuOption: AppsMenuOption): Unit
  def loadContacts(contactsMenuOption: ContactsMenuOption): Unit
}
