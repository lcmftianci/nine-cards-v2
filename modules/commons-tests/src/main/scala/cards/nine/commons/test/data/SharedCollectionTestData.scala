package cards.nine.commons.test.data

import cards.nine.commons.test.data.SharedCollectionValues._
import cards.nine.models.{SharedCollectionPackage, SharedCollection}

trait SharedCollectionTestData extends CollectionTestData {

  def sharedCollectionPackage(num: Int = 0) = SharedCollectionPackage(
    packageName = sharedCollectionPackageName,
    title = sharedCollectionPackageTitle,
    icon = sharedCollectionPackageIcon,
    stars = sharedCollectionPackageStars,
    downloads = sharedCollectionDownloads,
    free = sharedCollectionFree)

  val sharedCollectionPackage: SharedCollectionPackage = sharedCollectionPackage(0)
  val seqSharedCollectionPackage: Seq[SharedCollectionPackage] = Seq(sharedCollectionPackage(0), sharedCollectionPackage(1), sharedCollectionPackage(2))

  def sharedCollection(num: Int = 0) = SharedCollection(
    id = num.toString,
    sharedCollectionId = sharedCollectionId + num,
    publishedOn = publishedOn,
    author = author,
    name = sharedCollectionName,
    packages = sharedCollectionPackagesStr,
    resolvedPackages = seqSharedCollectionPackage,
    views = views,
    subscriptions = Some(subscriptions),
    category = sharedCollectionCategory,
    icon = sharedCollectionIcon,
    community = community,
    publicCollectionStatus = sharedCollectionPublicCollectionStatus)

  val sharedCollection: SharedCollection = sharedCollection(0)
  val seqSharedCollection: Seq[SharedCollection] = Seq(sharedCollection(0), sharedCollection(1), sharedCollection(2))

  val publicationListIds = seqSharedCollection.map(_.sharedCollectionId)

  val seqPublicCollection =
    seqCollection.flatMap(collection => collection.originalSharedCollectionId.map((_, collection))).filter{
      case (sharedCollectionId: String, _) => !publicationListIds.contains(sharedCollectionId)
    }

}
