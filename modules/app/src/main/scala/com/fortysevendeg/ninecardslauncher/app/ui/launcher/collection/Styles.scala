package com.fortysevendeg.ninecardslauncher.app.ui.launcher.collection

import android.widget.{ImageView, LinearLayout}
import com.fortysevendeg.macroid.extras.DeviceVersion.Lollipop
import com.fortysevendeg.macroid.extras.ImageViewTweaks._
import com.fortysevendeg.macroid.extras.LinearLayoutTweaks._
import com.fortysevendeg.macroid.extras.ResourcesExtras._
import com.fortysevendeg.macroid.extras.ViewTweaks._
import com.fortysevendeg.ninecardslauncher.app.ui.commons.CommonsTweak._
import com.fortysevendeg.ninecardslauncher.app.ui.commons.ExtraTweaks._
import com.fortysevendeg.ninecardslauncher.app.ui.commons.FabButtonTags._
import com.fortysevendeg.ninecardslauncher.app.ui.components.layouts.{WorkSpaceItemMenu, FabItemMenu}
import com.fortysevendeg.ninecardslauncher.app.ui.components.layouts.tweaks.WorkSpaceItemMenuTweaks._
import com.fortysevendeg.ninecardslauncher.app.ui.components.widgets.TintableImageView
import com.fortysevendeg.ninecardslauncher.app.ui.components.widgets.tweaks.TintableImageViewTweaks._
import com.fortysevendeg.ninecardslauncher.app.ui.launcher.LauncherTags
import com.fortysevendeg.ninecardslauncher.process.theme.models._
import com.fortysevendeg.ninecardslauncher2.R
import macroid.{Tweak, ContextWrapper}

trait Styles {

  def searchContentStyle(implicit context: ContextWrapper, theme: NineCardsTheme): Tweak[LinearLayout] =
    vBackgroundBoxWorkspace(theme.get(SearchBackgroundColor))

  def burgerButtonStyle(implicit context: ContextWrapper, theme: NineCardsTheme): Tweak[TintableImageView] =
    tivDefaultColor(theme.get(SearchIconsColor)) +
      tivPressedColor(theme.get(SearchPressedColor))

  def googleButtonStyle(implicit context: ContextWrapper, theme: NineCardsTheme): Tweak[TintableImageView] =
    tivDefaultColor(theme.get(SearchGoogleColor)) +
      tivPressedColor(theme.get(SearchPressedColor))

  def micButtonStyle(implicit context: ContextWrapper, theme: NineCardsTheme): Tweak[TintableImageView] =
    tivDefaultColor(theme.get(SearchIconsColor)) +
      tivPressedColor(theme.get(SearchPressedColor))

  def menuAvatarStyle(implicit context: ContextWrapper): Tweak[ImageView] =
    Lollipop ifSupportedThen {
      vCircleOutlineProvider()
    } getOrElse Tweak.blank

  def drawerItemStyle(implicit context: ContextWrapper, theme: NineCardsTheme): Tweak[TintableImageView] =
    tivPressedColor(theme.get(AppDrawerPressedColor)) +
      vTag(R.id.`type`, LauncherTags.app)

  def paginationItemStyle(implicit context: ContextWrapper): Tweak[ImageView] = {
    val margin = resGetDimensionPixelSize(R.dimen.margin_pager_collection)
    vWrapContent +
      llLayoutMargin(margin, margin, margin, margin) +
      ivSrc(R.drawable.workspaces_pager)
  }

  def workspaceButtonCreateCollectionStyle(implicit context: ContextWrapper): Tweak[WorkSpaceItemMenu] =
    workspaceButton(R.string.createNewCollection,
      R.drawable.fab_menu_icon_create_new_collection,
      R.color.collection_fab_button_item_create_new_collection)

  def workspaceButtonMyCollectionsStyle(implicit context: ContextWrapper): Tweak[WorkSpaceItemMenu] =
    workspaceButton(R.string.myCollections,
      R.drawable.fab_menu_icon_my_collections,
      R.color.collection_fab_button_item_my_collections)

  def workspaceButtonPublicCollectionStyle(implicit context: ContextWrapper): Tweak[WorkSpaceItemMenu] =
    workspaceButton(R.string.publicCollections,
      R.drawable.fab_menu_icon_public_collections,
      R.color.collection_fab_button_item_public_collection)

  private[this] def workspaceButton(title: Int, icon: Int, color: Int)(implicit context: ContextWrapper): Tweak[WorkSpaceItemMenu] =
    vWrapContent +
      wimBackgroundColor(resGetColor(color)) +
      wimTitle(resGetString(title)) +
      wimSrc(icon)

}