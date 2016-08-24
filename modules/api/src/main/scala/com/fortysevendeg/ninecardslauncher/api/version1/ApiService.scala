package com.fortysevendeg.ninecardslauncher.api.version1

import com.fortysevendeg.ninecardslauncher.commons.services.CatsService.CatsService
import com.fortysevendeg.rest.client.ServiceClient
import com.fortysevendeg.rest.client.messages.ServiceClientResponse
import play.api.libs.json.{Reads, Writes}

class ApiService(serviceClient: ServiceClient) {

  val prefixPathUser = "/users"

  val prefixPathUserConfig = "/ninecards/userconfig"

  def login(
    user: User,
    headers: Seq[(String, String)])
    (implicit reads: Reads[User],writes: Writes[User]): CatsService[ServiceClientResponse[User]] =
    serviceClient.post[User, User](
      path = prefixPathUser,
      headers = headers,
      body = user,
      reads = Some(reads))

  def getUserConfig(
    headers: Seq[(String, String)]
  )(implicit reads: Reads[UserConfig]): CatsService[ServiceClientResponse[UserConfig]] =
    serviceClient.get[UserConfig](
      path = prefixPathUserConfig,
      headers = headers,
      reads = Some(reads))

}
