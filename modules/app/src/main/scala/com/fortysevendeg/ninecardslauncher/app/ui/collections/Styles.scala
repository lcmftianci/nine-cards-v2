package com.fortysevendeg.ninecardslauncher.app.ui.collections

import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable._
import android.support.v4.app.Fragment
import android.support.v7.widget.{CardView, RecyclerView}
import android.text.TextUtils.TruncateAt
import android.view.ViewGroup.LayoutParams._
import android.view.{Gravity, View, ViewGroup}
import android.widget.ImageView.ScaleType
import android.widget.{FrameLayout, ImageView, LinearLayout, TextView}
import com.fortysevendeg.macroid.extras.CardViewTweaks._
import com.fortysevendeg.macroid.extras.DeviceVersion._
import com.fortysevendeg.macroid.extras.FrameLayoutTweaks._
import com.fortysevendeg.macroid.extras.LinearLayoutTweaks._
import com.fortysevendeg.macroid.extras.ResourcesExtras._
import com.fortysevendeg.macroid.extras.TextTweaks._
import com.fortysevendeg.macroid.extras.ViewGroupTweaks._
import com.fortysevendeg.macroid.extras.ViewTweaks._
import com.fortysevendeg.macroid.extras.ImageViewTweaks._
import com.fortysevendeg.ninecardslauncher.app.ui.commons.AsyncImageTweaks._
import com.fortysevendeg.ninecardslauncher.app.ui.commons.ColorsUtils._
import com.fortysevendeg.ninecardslauncher.app.ui.commons.UiContext
import com.fortysevendeg.ninecardslauncher.app.ui.components.FabItemMenuTweaks._
import com.fortysevendeg.ninecardslauncher.app.ui.components.{FabItemMenu, SlidingTabLayout}
import com.fortysevendeg.ninecardslauncher.app.ui.components.SlidingTabLayoutTweaks._
import com.fortysevendeg.ninecardslauncher.process.collection.models.Card
import com.fortysevendeg.ninecardslauncher.process.commons.CardType._
import com.fortysevendeg.ninecardslauncher.process.theme.models._
import com.fortysevendeg.ninecardslauncher2.R
import com.fortysevendeg.ninecardslauncher.app.ui.commons.FabButtonTags._
import macroid.FullDsl._
import macroid.{ActivityContextWrapper, ContextWrapper, Tweak}
import com.fortysevendeg.ninecardslauncher.app.ui.commons.ExtraTweaks._

trait Styles {

  def rootStyle(implicit context: ContextWrapper, theme: NineCardsTheme): Tweak[FrameLayout] =
    vBackgroundColor(theme.get(CollectionDetailBackgroundColor))

  def tabsStyle(implicit context: ContextWrapper, theme: NineCardsTheme): Tweak[SlidingTabLayout] =
    stlDefaultTextColor(theme.get(CollectionDetailTextTabDefaultColor)) +
      stlSelectedTextColor(theme.get(CollectionDetailTextTabSelectedColor))

  def fragmentContentStyle(clickable: Boolean): Tweak[FrameLayout] = Tweak[View]( _.setClickable(clickable))

  def fabButtonApplicationsStyle(implicit context: ContextWrapper): Tweak[FabItemMenu] =
    fabButton(R.string.applications, R.drawable.fab_menu_icon_applications, 1)

  def fabButtonRecommendationsStyle(implicit context: ContextWrapper): Tweak[FabItemMenu] =
    fabButton(R.string.recommendations, R.drawable.fab_menu_icon_recommendations, 2)

  def fabButtonContactsStyle(implicit context: ContextWrapper): Tweak[FabItemMenu] =
    fabButton(R.string.contacts, R.drawable.fab_menu_icon_contact, 3)

  def fabButtonShortcutsStyle(implicit context: ContextWrapper): Tweak[FabItemMenu] =
    fabButton(R.string.shortcuts, R.drawable.fab_menu_icon_shorcut, 4)

  private[this] def fabButton(title: Int, icon: Int, tag: Int)(implicit context: ContextWrapper): Tweak[FabItemMenu] =
    vWrapContent +
      fimBackgroundColor(resGetColor(R.color.collection_detail_fab_button_item)) +
      fimTitle(resGetString(title)) +
      fimSrc(icon) +
      vGone +
      vTag(R.id.`type`, fabButtonItem) +
      vIntTag(R.id.fab_menu_position, tag)

}

trait CollectionFragmentStyles {

  def recyclerStyle(implicit context: ContextWrapper): Tweak[RecyclerView] = {
    val paddingTop = resGetDimensionPixelSize(R.dimen.space_moving_collection_details)
    val padding = resGetDimensionPixelSize(R.dimen.padding_small)
    vMatchParent +
      vPadding(padding, paddingTop, padding, padding) +
      vgClipToPadding(false) +
      vOverScrollMode(View.OVER_SCROLL_NEVER)
  }

}

trait CollectionAdapterStyles {

  val iconContentHeightRatio = .6f

  val alphaDefault = .1f

  def rootStyle(heightCard: Int)(implicit context: ContextWrapper, theme: NineCardsTheme): Tweak[CardView] =
    vContentSizeMatchWidth(heightCard) +
      cvCardBackgroundColor(theme.get(CollectionDetailCardBackgroundColor)) +
      flForeground(createBackground)

  private def createBackground(implicit context: ContextWrapper, theme: NineCardsTheme): Drawable = {
    val color = theme.get(CollectionDetailCardBackgroundPressedColor)
    Lollipop ifSupportedThen {
      new RippleDrawable(
        new ColorStateList(Array(Array()), Array(color)),
        null,
        new ColorDrawable(setAlpha(Color.BLACK, alphaDefault)))
    } getOrElse {
      val states = new StateListDrawable()
      states.addState(Array[Int](android.R.attr.state_pressed), new ColorDrawable(setAlpha(color, alphaDefault)))
      states.addState(Array.emptyIntArray, new ColorDrawable(Color.TRANSPARENT))
      states
    }
  }

  def iconContentStyle(heightCard: Int)(implicit context: ContextWrapper): Tweak[FrameLayout] =
    lp[ViewGroup](MATCH_PARENT, (heightCard * iconContentHeightRatio).toInt)

  def contentStyle(implicit context: ContextWrapper): Tweak[LinearLayout] =
    vMatchParent +
      llVertical

  def iconStyle(implicit context: ContextWrapper): Tweak[ImageView] = {
    val size = resGetDimensionPixelSize(R.dimen.size_icon_card)
    lp[ViewGroup](size, size) +
      flLayoutGravity(Gravity.CENTER)
  }

  def nameStyle(implicit context: ContextWrapper, theme: NineCardsTheme): Tweak[TextView] =
    vMatchWidth +
      vPaddings(resGetDimensionPixelSize(R.dimen.padding_default)) +
      tvColor(theme.get(CollectionDetailTextCardColor)) +
      tvLines(2) +
      tvSizeResource(R.dimen.text_default) +
      tvEllipsize(TruncateAt.END)

  def iconCardTransform(card: Card)(implicit context: ActivityContextWrapper, uiContext: UiContext[_]) = card.cardType match {
    case `phone` | `sms` | `email` =>
      ivUriContact(card.imagePath, card.term) +
        expandLayout +
        ivScaleType(ScaleType.CENTER_CROP)
    case _ =>
      ivCardUri(card.imagePath, card.term) +
        reduceLayout +
        ivScaleType(ScaleType.FIT_CENTER)
  }

  def expandLayout(implicit context: ContextWrapper): Tweak[View] = Tweak[View] {
    view =>
      val params = view.getLayoutParams
      params.height = MATCH_PARENT
      params.width = MATCH_PARENT
      view.requestLayout()
  }

  def reduceLayout(implicit context: ContextWrapper): Tweak[View] = Tweak[View] {
    view =>
      val size = resGetDimensionPixelSize(R.dimen.size_icon_card)
      val params = view.getLayoutParams
      params.height = size
      params.width = size
      view.requestLayout()
  }

}
