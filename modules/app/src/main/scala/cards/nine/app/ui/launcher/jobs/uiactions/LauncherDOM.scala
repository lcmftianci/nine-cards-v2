/*
 * Copyright 2017 47 Degrees, LLC. <http://www.47deg.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cards.nine.app.ui.launcher.jobs.uiactions

import android.app.Activity
import android.support.v4.app.{Fragment, FragmentManager}
import android.support.v4.view.GravityCompat
import android.view.View
import cards.nine.app.ui.commons.ActivityFindViews
import cards.nine.app.ui.commons.dialogs.BaseActionFragment
import cards.nine.app.ui.commons.ops.ViewOps._
import cards.nine.app.ui.components.layouts.tweaks.LauncherWorkSpacesTweaks._
import cards.nine.app.ui.components.layouts.tweaks.TabsViewTweaks._
import cards.nine.app.ui.components.models.LauncherData
import cards.nine.app.ui.components.widgets.{AppsView, ContentView}
import cards.nine.app.ui.launcher.types.AppsAlphabetical
import cards.nine.models.{Collection, Widget}
import cards.nine.models.types.NineCardsMoment
import com.fortysevendeg.ninecardslauncher.TR
import macroid._
import macroid.extras.FragmentExtras._

class LauncherDOM(activity: Activity) {

  import ActivityFindViews._

  val nameActionFragment = "action-fragment"

  val searchingGooglePlayKey = "searching-google-play-key"

  val emptyInfoKey = "empty-info-key"

  lazy val foreground = findView(TR.launcher_foreground).run(activity)

  lazy val appsMoment = findView(TR.launcher_apps_moment).run(activity)

  lazy val drawerLayout = findView(TR.launcher_drawer_layout).run(activity)

  lazy val navigationView = findView(TR.launcher_navigation_view).run(activity)

  lazy val menuName = findView(TR.menu_name).run(activity)

  lazy val menuEmail = findView(TR.menu_email).run(activity)

  lazy val menuAvatar = findView(TR.menu_avatar).run(activity)

  lazy val menuHeader = findView(TR.menu_header).run(activity)

  lazy val menuCover = findView(TR.menu_cover).run(activity)

  lazy val loading = findView(TR.launcher_loading).run(activity)

  lazy val root = findView(TR.launcher_root).run(activity)

  lazy val dockAppsPanel = findView(TR.launcher_dock_apps_panel).run(activity)

  lazy val content = findView(TR.launcher_content).run(activity)

  lazy val workspaces = findView(TR.launcher_work_spaces).run(activity)

  lazy val workspacesEdgeLeft =
    findView(TR.launcher_work_spaces_edge_left).run(activity)

  lazy val workspacesEdgeRight =
    findView(TR.launcher_work_spaces_edge_right).run(activity)

  lazy val paginationPanel =
    findView(TR.launcher_pagination_panel).run(activity)

  lazy val topBarPanel = findView(TR.launcher_top_bar_panel).run(activity)

  lazy val collectionActionsPanel =
    findView(TR.launcher_collections_actions_panel).run(activity)

  lazy val menuCollectionRoot = findView(TR.menu_collection_root).run(activity)

  lazy val menuWorkspaceContent =
    findView(TR.menu_workspace_content).run(activity)

  lazy val menuLauncherContent =
    findView(TR.menu_launcher_content).run(activity)

  lazy val menuLauncherWallpaper =
    findView(TR.menu_launcher_wallpaper).run(activity)

  lazy val menuLauncherWidgets =
    findView(TR.menu_launcher_widgets).run(activity)

  lazy val menuLauncherSettings =
    findView(TR.menu_launcher_settings).run(activity)

  lazy val appDrawerMain = findView(TR.launcher_app_drawer).run(activity)

  lazy val drawerContent = findView(TR.launcher_drawer_content).run(activity)

  lazy val drawerMessage = findView(TR.launcher_drawer_message).run(activity)

  lazy val scrollerLayout =
    findView(TR.launcher_drawer_scroller_layout).run(activity)

  lazy val recycler = findView(TR.launcher_drawer_recycler).run(activity)

  lazy val tabs = findView(TR.launcher_drawer_tabs).run(activity)

  lazy val pullToTabsView =
    findView(TR.launcher_drawer_pull_to_tabs).run(activity)

  lazy val searchBoxView =
    findView(TR.launcher_search_box_content).run(activity)

  def getWorksSpacesCount: Int = workspaces.getWorksSpacesCount

  def getData: Seq[LauncherData] = workspaces.data

  def getCurrentWidgets: Seq[Widget] = workspaces.getWidgets

  def hasCurrentMomentAssociatedCollection: Boolean =
    (getData.headOption flatMap (_.moment) flatMap (_.collection)).isDefined

  def getCurrentMomentType: Option[NineCardsMoment] =
    getData.headOption flatMap (_.moment) flatMap (_.momentType)

  def getCurrentMomentTypeName: Option[String] =
    getCurrentMomentType map (_.name)

  def isMenuVisible: Boolean = drawerLayout.isDrawerOpen(GravityCompat.START)

  def isAppsByMomentMenuVisible: Boolean =
    drawerLayout.isDrawerOpen(GravityCompat.END)

  def isBackgroundMenuVisible: Boolean =
    workspaces.workSpacesStatuses.openedMenu

  def isDrawerTabsOpened: Boolean = (tabs ~> isOpened).get

  def getStatus: Option[String] = recycler.getType

  def getTypeView: Option[ContentView] = Option(recycler.statuses.contentView)

  def isDrawerShowingApps: Boolean = getTypeView.contains(AppsView)

  def getItemsCount: Int =
    Option(recycler.getAdapter) map (_.getItemCount) getOrElse 0

  def getDrawerWidth: Int = drawerContent.getWidth

  def isDrawerVisible: Boolean = drawerContent.getVisibility == View.VISIBLE

  def isSearchingInGooglePlay: Boolean =
    searchBoxView.getField[Boolean](searchingGooglePlayKey) getOrElse false

  def isShowingEmptyInfo: Boolean =
    searchBoxView.getField[Boolean](emptyInfoKey) getOrElse false

  def isEmptyCollections: Boolean = (workspaces ~> lwsEmptyCollections).get

  def isEmptySearchBox: Boolean = searchBoxView.isEmpty

  def isShowingAppsAlphabetical: Boolean =
    recycler.isType(AppsAlphabetical.name)

  def isCollectionWorkspace: Boolean =
    (workspaces ~> lwsIsCollectionWorkspace).get

  def isWorkspaceScrolling: Boolean =
    workspaces.animatedWorkspaceStatuses.isScrolling

  def getCollections: Seq[Collection] = (workspaces ~> lwsGetCollections()).get

  def getCollection(position: Int): Option[Collection] =
    getCollections.lift(position)

  def getCountCollections: Int = (workspaces ~> lwsCountCollections).get

  def canRemoveCollections: Boolean = getCountCollections > 1

  def isActionShowed(implicit fragmentManagerContext: FragmentManagerContext[
    Fragment,
    FragmentManager]): Boolean = findFragmentByTag(nameActionFragment).isDefined

  def getFragment(
      implicit fragmentManagerContext: FragmentManagerContext[Fragment, FragmentManager]): Option[
    BaseActionFragment] =
    findFragmentByTag[BaseActionFragment](nameActionFragment)

}
