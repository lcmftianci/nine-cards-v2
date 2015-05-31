package com.fortysevendeg.ninecardslauncher.modules.api

import com.fortysevendeg.ninecardslauncher.models._

case class LoginRequest(
    email: String,
    device: GoogleDevice)

case class LoginResponse(
    statusCode: Int,
    user: Option[User])

case class LinkGoogleAccountRequest(
    deviceId: String,
    token: String,
    email: String,
    devices: Seq[GoogleDevice])

case class InstallationRequest(
    id: Option[String],
    deviceType: Option[String],
    deviceToken: Option[String],
    userId: Option[String])

case class InstallationResponse(
    statusCode: Int,
    installation: Option[Installation])

case class UpdateInstallationResponse(
    statusCode: Int)

case class GooglePlayPackageRequest(
    deviceId: String,
    token: String,
    packageName: String)

case class GooglePlayPackageResponse(
    statusCode: Int,
    app: Option[GooglePlayApp])

case class GooglePlayPackagesRequest(
    deviceId: String,
    token: String,
    packageNames: Seq[String])

case class GooglePlayPackagesResponse(
    statusCode: Int,
    packages: Seq[GooglePlayPackage])

case class GooglePlaySimplePackagesRequest(
    deviceId: String,
    token: String,
    items: Seq[String])

case class GooglePlaySimplePackagesResponse(
    statusCode: Int,
    apps: GooglePlaySimplePackages)

trait UserConfigRequest {
    def deviceId: String
    def token: String
}

trait UserConfigResponse {
    def statusCode: Int
    def userConfig: Option[UserConfig]
}

case class GetUserConfigRequest(
    deviceId: String,
    token: String) extends UserConfigRequest

case class GetUserConfigResponse(
    statusCode: Int,
    userConfig: Option[UserConfig]) extends UserConfigResponse

case class SaveDeviceRequest(
    deviceId: String,
    token: String,
    userConfigDevice: UserConfigDevice) extends UserConfigRequest

case class SaveDeviceResponse(
    statusCode: Int,
    userConfig: Option[UserConfig]) extends UserConfigResponse

case class SaveGeoInfoRequest(
    deviceId: String,
    token: String,
    userConfigGeoInfo: UserConfigGeoInfo) extends UserConfigRequest

case class SaveGeoInfoResponse(
    statusCode: Int,
    userConfig: Option[UserConfig]) extends UserConfigResponse

case class CheckpointPurchaseProductRequest(
    deviceId: String,
    token: String,
    productId: String) extends UserConfigRequest

case class CheckpointPurchaseProductResponse(
    statusCode: Int,
    userConfig: Option[UserConfig]) extends UserConfigResponse

case class CheckpointCustomCollectionRequest(
    deviceId: String,
    token: String) extends UserConfigRequest

case class CheckpointCustomCollectionResponse(
    statusCode: Int,
    userConfig: Option[UserConfig]) extends UserConfigResponse

case class CheckpointJoinedByRequest(
    deviceId: String,
    token: String,
    otherConfigId: String) extends UserConfigRequest

case class CheckpointJoinedByResponse(
    statusCode: Int,
    userConfig: Option[UserConfig]) extends UserConfigResponse

case class TesterRequest(
    deviceId: String,
    token: String,
    replace: Map[String, String]) extends UserConfigRequest

case class TesterResponse(
    statusCode: Int,
    userConfig: Option[UserConfig]) extends UserConfigResponse