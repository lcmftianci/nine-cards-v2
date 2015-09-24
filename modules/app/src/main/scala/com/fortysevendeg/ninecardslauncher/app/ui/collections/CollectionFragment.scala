package com.fortysevendeg.ninecardslauncher.app.ui.collections

import android.app.Activity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.{LayoutInflater, View, ViewGroup}
import com.fortysevendeg.ninecardslauncher.app.commons.ContextSupportProvider
import com.fortysevendeg.ninecardslauncher.app.di.Injector
import com.fortysevendeg.ninecardslauncher.app.ui.collections.CollectionFragment._
import com.fortysevendeg.ninecardslauncher.app.ui.commons.AppUtils._
import com.fortysevendeg.ninecardslauncher.app.ui.commons.Constants._
import com.fortysevendeg.ninecardslauncher.app.ui.commons.{FragmentUiContext, UiContext}
import com.fortysevendeg.ninecardslauncher.process.collection.models.{Card, Collection}
import com.fortysevendeg.ninecardslauncher.process.theme.models.NineCardsTheme
import macroid.Contexts
import macroid.FullDsl._
import rapture.core.Answer

class CollectionFragment
  extends Fragment
  with Contexts[Fragment]
  with ContextSupportProvider
  with CollectionFragmentComposer {

  lazy val di = new Injector

  implicit lazy val theme: NineCardsTheme = di.themeProcess.getSelectedTheme.run.run match {
    case Answer(t) => t
    case _ => getDefaultTheme
  }

  implicit lazy val uiContext: UiContext[Fragment] = FragmentUiContext(this)

  lazy val animateCards = getArguments.getBoolean(keyAnimateCards, false)

  lazy val position = getArguments.getInt(keyPosition, 0)

  lazy val collection = getArguments.getSerializable(keyCollection).asInstanceOf[Collection]

  override def onCreateView(inflater: LayoutInflater, container: ViewGroup, savedInstanceState: Bundle): View =
    layout(animateCards)

  override def onViewCreated(view: View, savedInstanceState: Bundle): Unit = {
    sType = getArguments.getInt(keyScrollType, ScrollType.down)
    canScroll = collection.cards.length > numSpaces
    runUi(initUi(collection, animateCards))
    super.onViewCreated(view, savedInstanceState)
  }

  override def onAttach(activity: Activity): Unit = {
    super.onAttach(activity)
    activity match {
      case scroll: ScrolledListener => scrolledListener = Some(scroll)
      case _ =>
    }
  }

  override def onDetach(): Unit = {
    super.onDetach()
    scrolledListener = None
  }

  def bindAnimatedAdapter = if (animateCards) runUi(setAnimatedAdapter(collection))

  def addCards(cards: Seq[Card]) = getAdapter foreach { adapter =>
    adapter.addCards(cards)
    val cardCount = adapter.collection.cards.length
    canScroll = cardCount > numSpaces
    runUi(resetScroll(adapter.collection))
  }

  def removeCard(card: Card) = getAdapter foreach { adapter =>
    adapter.removeCard(card)
    val cardCount = adapter.collection.cards.length
    canScroll = cardCount > numSpaces
    runUi(resetScroll(adapter.collection))
  }
}

object CollectionFragment {
  val keyPosition = "tab_position"
  val keyCollection = "collection"
  val keyScrollType = "scroll_type"
  val keyAnimateCards = "animate_cards"
}

