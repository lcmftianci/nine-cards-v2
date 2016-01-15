package com.fortysevendeg.ninecardslauncher.process.cloud.impl

import com.fortysevendeg.ninecardslauncher.commons.NineCardExtensions._
import com.fortysevendeg.ninecardslauncher.commons.services.Service
import com.fortysevendeg.ninecardslauncher.commons.services.Service.ServiceDef2
import com.fortysevendeg.ninecardslauncher.process.cloud.{ImplicitsCloudStorageProcessExceptions, CloudStorageProcessException, Conversions, CloudStorageProcess}
import com.fortysevendeg.ninecardslauncher.process.cloud.models.CloudStorageImplicits
import com.fortysevendeg.ninecardslauncher.process.cloud.models.CloudStorageDevice
import com.fortysevendeg.ninecardslauncher.process.cloud.ImplicitsCloudStorageProcessExceptions
import com.fortysevendeg.ninecardslauncher.services.drive.{DriveServicesException, DriveServices}
import com.fortysevendeg.ninecardslauncher.services.drive.models.DriveServiceFile
import CloudStorageImplicits._
import play.api.libs.json.Json
import rapture.core.{Errata, Answer}

import scala.util.{Failure, Success, Try}
import scalaz.Scalaz._
import scalaz.concurrent.Task

class CloudStorageProcessImpl(driveServices: DriveServices)
  extends CloudStorageProcess
  with Conversions
  with ImplicitsCloudStorageProcessExceptions {


  private[this] val userDeviceType = "USER_DEVICE"

  private[this] val jsonMimeType = "application/json"

  override def getCloudStorageDevices() =
    (for {
      driveServiceSeq <- driveServices.listFiles(userDeviceType.some)
    } yield driveServiceSeq map toDriveDevice).resolve[CloudStorageProcessException]

  override def getCloudStorageDevice(cloudStorageResourceId: String) =
    (for {
      json <- driveServices.readFile(cloudStorageResourceId)
      device <- parseCloudStorageDevice(json)
    } yield device).resolve[CloudStorageProcessException]

  override def createOrUpdateCloudStorageDevice(cloudStorageDevice: CloudStorageDevice) = {
    (for {
      file <- driveServices.findFile(cloudStorageDevice.deviceId)
      json <- cloudStorageDeviceToJson(cloudStorageDevice)
      _ <- createOrUpdateFile(file, cloudStorageDevice.deviceName, json, cloudStorageDevice.deviceId)
    } yield ()).resolve[CloudStorageProcessException]
  }

  private[this] def parseCloudStorageDevice(json: String): ServiceDef2[CloudStorageDevice, CloudStorageProcessException] = Service {
    Task {
      Try(Json.parse(json).as[CloudStorageDevice]) match {
        case Success(s) => Answer(s)
        case Failure(e) => Errata(CloudStorageProcessException(message = e.getMessage, cause = e.some))
      }
    }
  }

  private[this] def cloudStorageDeviceToJson(cloudStorageDevice: CloudStorageDevice): ServiceDef2[String, CloudStorageProcessException] = Service {
    Task {
      Try(Json.toJson(cloudStorageDevice).toString()) match {
        case Success(s) => Answer(s)
        case Failure(e) => Errata(CloudStorageProcessException(message = e.getMessage, cause = Some(e)))
      }
    }
  }

  private[this] def createOrUpdateFile(maybeDriveFile: Option[DriveServiceFile], title: String, content: String, fileId: String): ServiceDef2[Unit, DriveServicesException] = {
    maybeDriveFile match {
      case Some(driveFile) => driveServices.updateFile(driveFile.driveId, content)
      case _ => driveServices.createFile(title, content, fileId, userDeviceType, jsonMimeType)
    }
  }
}
