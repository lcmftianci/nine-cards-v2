package com.fortysevendeg.ninecardslauncher.services.api.impl

import com.fortysevendeg.ninecardslauncher.api._
import com.fortysevendeg.ninecardslauncher.api.version1.model._
import com.fortysevendeg.ninecardslauncher.api.version2.CategorizedApp

import scala.util.Random

trait ApiServicesImplData {

  def generateUser =
    User(
      _id = Some(Random.nextString(10)),
      sessionToken = Some(Random.nextString(10)),
      email = Some(Random.nextString(10)),
      username = Some(Random.nextString(10)),
      password = Some(Random.nextString(10)),
      authData = Some(generateAuthData))

  def generateAuthData =
    AuthData(
      google = Some(generateAuthGoogle),
      facebook = None,
      twitter = None,
      anonymous = None)

  def generateAuthGoogle =
    AuthGoogle(
      email = Random.nextString(10),
      devices = generateAuthGoogleDevices(2))

  def generateAuthGoogleDevices(num: Int = 10) =
    1 to num map { _ =>
      AuthGoogleDevice(
        name = Random.nextString(10),
        deviceId = Random.nextString(10),
        secretToken = Random.nextString(10),
        permissions = generatePermissions())
    }

  def generatePermissions(num: Int = 10): Seq[String] = 1 to num map { n => s"Permission $n" }

  def generateInstallation =
    Installation(
      _id = Some(Random.nextString(10)),
      deviceType = Some(Random.nextString(10)),
      deviceToken = Some(Random.nextString(10)),
      userId = Some(Random.nextString(10)))

  def generateCategorizedApps(num: Int = 10) =
    1 to num map { _ =>
      generateCategorizedApp
    }

  def generateCategorizedApp =
    CategorizedApp(
      packageName = Random.nextString(10),
      category = generateCategories(1).head)

  def generateGooglePlayPackages =
    GooglePlayPackages(
      Seq.empty,
      generateGooglePlayPackageSeq())

  def generateGooglePlayPackageSeq(num: Int = 10) =
    1 to num map { _ =>
      GooglePlayPackage(generateGooglePlayApp)
    }

  def generateGooglePlayApp =
    GooglePlayApp(
      docid = Random.nextString(10),
      title = Random.nextString(10),
      creator = Random.nextString(10),
      descriptionHtml = Some(Random.nextString(10)),
      image = Seq.empty,
      details = generateGooglePlayDetails,
      offer = Seq.empty,
      generateGooglePlayAggregateRating)

  def generateGooglePlayDetails =
    GooglePlayDetails(generateGooglePlayAppDetails)

  def generateGooglePlayAppDetails =
    GooglePlayAppDetails(
      appCategory = generateCategories(),
      numDownloads = Random.nextInt(10).toString,
      developerEmail = Some(Random.nextString(10)),
      developerName = Some(Random.nextString(10)),
      developerWebsite = Some(Random.nextString(10)),
      versionCode = Some(Random.nextInt(10)),
      versionString = Some(Random.nextString(10)),
      appType = Some(Random.nextString(10)),
      permission = generatePermissions())

  def generateCategories(num: Int = 10): Seq[String] = 1 to num map { n => s"Category $n" }

  def generateGooglePlayAggregateRating =
    GooglePlayAggregateRating(
      ratingsCount = Random.nextInt(10),
      commentCount = Some(Random.nextInt(10)),
      oneStarRatings = Random.nextInt(10),
      twoStarRatings = Random.nextInt(10),
      threeStarRatings = Random.nextInt(10),
      fourStarRatings = Random.nextInt(10),
      fiveStarRatings = Random.nextInt(10),
      starRating = Random.nextDouble())

  def generateUserConfig =
    UserConfig(
      Random.nextString(10),
      Random.nextString(10),
      generateUserConfigPlusProfile,
      generateUserConfigDeviceSeq(),
      generateUserConfigGeoInfo,
      generateUserConfigStatusInfo)

  def generateUserConfigPlusProfile =
    UserConfigPlusProfile(
      Random.nextString(10),
      generateUserConfigProfileImage)

  def generateUserConfigProfileImage =
    UserConfigProfileImage(
      imageType = Random.nextInt(10),
      imageUrl = Random.nextString(10),
      secureUrl = Option(Random.nextString(10)))

  def generateUserConfigDeviceSeq(num: Int = 10) =
    1 to num map { _ =>
      UserConfigDevice(
        Random.nextString(10),
        Random.nextString(10),
        Seq.empty)
    }

  def generateUserConfigGeoInfo =
    UserConfigGeoInfo(
      homeMorning = None,
      homeNight = None,
      work = None,
      current = None)

  def generateUserConfigStatusInfo =
    UserConfigStatusInfo(
      products = Seq.empty,
      friendsReferred = Random.nextInt(10),
      themesShared = Random.nextInt(10),
      collectionsShared = Random.nextInt(10),
      customCollections = Random.nextInt(10),
      earlyAdopter = false,
      communityMember = false,
      joinedThrough = None,
      tester = false)

  def generateGooglePlayRecommendation(googlePlayApps: Seq[GooglePlayApp]) =
    GooglePlayRecommendation(
      googlePlayApps.size,
      googlePlayApps map (app => GooglePlayRecommendationItems(app.docid, app, None))
    )

  def generateCollection(collectionApps: Seq[version2.CollectionApp]) =
    version2.Collection(
      name = Random.nextString(10),
      author = Random.nextString(10),
      description = Some(Random.nextString(10)),
      icon = Random.nextString(10),
      category = "SOCIAL",
      community = Random.nextBoolean(),
      publishedOn = "\"2016-08-19T09:39:00.359000\"",
      installations = Some(Random.nextInt(10)),
      views = Some(Random.nextInt(100)),
      publicIdentifier = Random.nextString(10),
      appsInfo = collectionApps,
      packages = collectionApps map (_.packageName))

  def generateCollectionApp =
    version2.CollectionApp(
      stars = Random.nextDouble() * 5,
      icon = Random.nextString(10),
      packageName = Random.nextString(10),
      downloads = "500,000,000+",
      category = "SOCIAL",
      title = Random.nextString(10),
      free = Random.nextBoolean())

  val offset = 0

  val limit = 20

  val category = "COMMUNICATION"

  val name = "Name"

  val description = "Description"

  val author = "Author"

  val packages = List("Package1", "Package2")

  val icon = "Icon"

  val community = true

  val collectionTypeTop = "top"
  val collectionTypeLatest = "latest"

  val user = generateUser

  val installation = generateInstallation

  val categorizeApps = generateCategorizedApps()

  val googlePlayPackages = generateGooglePlayPackages

  val googlePlayApps = 1 to 10 map (_ => generateGooglePlayApp)

  val googlePlayRecommendation = generateGooglePlayRecommendation(googlePlayApps)

  val collectionApps1 = 1 to 5 map (_ => generateCollectionApp)
  val collectionApps2 = 1 to 5 map (_ => generateCollectionApp)
  val collectionApps3 = 1 to 5 map (_ => generateCollectionApp)

  val collections = Seq(
    generateCollection(collectionApps1),
    generateCollection(collectionApps2),
    generateCollection(collectionApps3))

  val userConfig = generateUserConfig

  val apiKey = Random.nextString(10)

  val sessionToken = Random.nextString(20)

  val deviceToken = Random.nextString(20)

  val email = "email@dot.com"

  val androidId = Random.nextString(10)

  val tokenId = Random.nextString(30)

  val sharedCollectionId = Random.nextString(30)

  val packageStats = version2.PackagesStats(1, None)
}
