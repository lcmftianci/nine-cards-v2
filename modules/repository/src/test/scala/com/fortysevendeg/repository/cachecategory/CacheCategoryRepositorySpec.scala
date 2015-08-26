package com.fortysevendeg.repository.cachecategory

import android.net.Uri
import com.fortysevendeg.ninecardslauncher.commons.contentresolver.Conversions._
import com.fortysevendeg.ninecardslauncher.commons.contentresolver.{ContentResolverWrapperImpl, UriCreator}
import com.fortysevendeg.ninecardslauncher.repository.RepositoryException
import com.fortysevendeg.ninecardslauncher.repository.model.CacheCategory
import com.fortysevendeg.ninecardslauncher.repository.provider.CacheCategoryEntity._
import com.fortysevendeg.ninecardslauncher.repository.provider._
import com.fortysevendeg.ninecardslauncher.repository.repositories._
import com.fortysevendeg.repository._
import org.specs2.matcher.DisjunctionMatchers
import org.specs2.mock.Mockito
import org.specs2.mutable.Specification
import org.specs2.specification.Scope
import rapture.core.{Answer, Errata}

trait CacheCategoryRepositorySpecification
  extends Specification
  with DisjunctionMatchers
  with Mockito {

  trait CacheCategoryRepositoryScope
    extends Scope {

    lazy val contentResolverWrapper = mock[ContentResolverWrapperImpl]

    lazy val uriCreator = mock[UriCreator]
    
    lazy val cacheCategoryRepository = new CacheCategoryRepository(contentResolverWrapper, uriCreator)

    lazy val mockUri = mock[Uri]
  }

  trait ValidCacheCategoryRepositoryResponses
    extends CacheCategoryRepositoryTestData {

    self: CacheCategoryRepositoryScope =>

    uriCreator.parse(any) returns mockUri

    contentResolverWrapper.insert(uri = mockUri, values = createCacheCategoryValues) returns testCacheCategoryId

    contentResolverWrapper.deleteById(uri = mockUri, id = testCacheCategoryId) returns 1

    contentResolverWrapper.delete(
      uri = mockUri,
      where = s"$packageName = ?",
      whereParams = Seq(testPackageName)) returns 1

    contentResolverWrapper.findById(
      uri = mockUri,
      id = testCacheCategoryId,
      projection = allFields)(
        f = getEntityFromCursor(cacheCategoryEntityFromCursor)) returns Some(cacheCategoryEntity)

    contentResolverWrapper.findById(
      uri = mockUri,
      id = testNonExistingCacheCategoryId,
      projection = allFields)(
        f = getEntityFromCursor(cacheCategoryEntityFromCursor)) returns None

    contentResolverWrapper.fetchAll(
      uri = mockUri,
      projection = allFields)(
        f = getListFromCursor(cacheCategoryEntityFromCursor)) returns cacheCategoryEntitySeq

    contentResolverWrapper.fetch(
      uri = mockUri,
      projection = allFields,
      where = s"$packageName = ?",
      whereParams = Seq(testPackageName))(
        f = getEntityFromCursor(cacheCategoryEntityFromCursor)) returns Some(cacheCategoryEntity)

    contentResolverWrapper.fetch(
      uri = mockUri,
      projection = allFields,
      where = s"$packageName = ?",
      whereParams = Seq(testNonExistingPackageName))(
        f = getEntityFromCursor(cacheCategoryEntityFromCursor)) returns None

    contentResolverWrapper.updateById(mockUri, testCacheCategoryId, createCacheCategoryValues) returns 1
  }

  trait ErrorCacheCategoryRepositoryResponses
    extends CacheCategoryRepositoryTestData {

    self: CacheCategoryRepositoryScope =>

    val contentResolverException = new RuntimeException("Irrelevant message")

    uriCreator.parse(any) returns mockUri

    contentResolverWrapper.insert(uri = mockUri, values = createCacheCategoryValues) throws contentResolverException

    contentResolverWrapper.deleteById(uri = mockUri, id = testCacheCategoryId) throws contentResolverException

    contentResolverWrapper.delete(
      uri = mockUri,
      where = s"$packageName = ?",
      whereParams = Seq(testPackageName)) throws contentResolverException

    contentResolverWrapper.findById(
      uri = mockUri,
      id = testCacheCategoryId,
      projection = allFields)(
        f = getEntityFromCursor(cacheCategoryEntityFromCursor)) throws contentResolverException

    contentResolverWrapper.fetchAll(
      uri = mockUri,
      projection = allFields)(
        f = getListFromCursor(cacheCategoryEntityFromCursor)) throws contentResolverException

    contentResolverWrapper.fetch(
      uri = mockUri,
      projection = allFields,
      where = s"$packageName = ?",
      whereParams = Seq(testPackageName))(
        f = getEntityFromCursor(cacheCategoryEntityFromCursor)) throws contentResolverException

    contentResolverWrapper.updateById(mockUri, testCacheCategoryId, createCacheCategoryValues) throws contentResolverException
  }

}

trait CacheCategoryMockCursor
  extends MockCursor
  with CacheCategoryRepositoryTestData {

  val cursorData = Seq(
    (NineCardsSqlHelper.id, 0, cacheCategorySeq map (_.id), IntDataType),
    (packageName, 1, cacheCategorySeq map (_.data.packageName), StringDataType),
    (category, 2, cacheCategorySeq map (_.data.category), StringDataType),
    (starRating, 3, cacheCategorySeq map (_.data.starRating), DoubleDataType),
    (numDownloads, 4, cacheCategorySeq map (_.data.numDownloads), StringDataType),
    (ratingsCount, 5, cacheCategorySeq map (_.data.ratingsCount), IntDataType),
    (commentCount, 6, cacheCategorySeq map (_.data.commentCount), IntDataType))

  prepareCursor[CacheCategory](cacheCategorySeq.size, cursorData)
}

trait EmptyCacheCategoryMockCursor
  extends MockCursor
  with CacheCategoryRepositoryTestData {

  val cursorData = Seq(
    (NineCardsSqlHelper.id, 0, Seq.empty, IntDataType),
    (packageName, 1, Seq.empty, StringDataType),
    (category, 2, Seq.empty, StringDataType),
    (starRating, 3, Seq.empty, DoubleDataType),
    (numDownloads, 4, Seq.empty, StringDataType),
    (ratingsCount, 5, Seq.empty, IntDataType),
    (commentCount, 6, Seq.empty, IntDataType))

  prepareCursor[CacheCategory](0, cursorData)
}

class CacheCategoryRepositorySpec
  extends CacheCategoryRepositorySpecification {

  "CacheCategoryRepositoryClient component" should {

    "addCacheCategory" should {

      "return a CacheCategory object with a valid request" in
        new CacheCategoryRepositoryScope
          with ValidCacheCategoryRepositoryResponses {

          val result = cacheCategoryRepository.addCacheCategory(data = createCacheCategoryData).run.run

          result must beLike {
            case Answer(cacheCategory) =>
              cacheCategory.id shouldEqual testCacheCategoryId
              cacheCategory.data.packageName shouldEqual testPackageName
          }
        }

      "return a RepositoryException when a exception is thrown" in
        new CacheCategoryRepositoryScope
          with ErrorCacheCategoryRepositoryResponses {

          val result = cacheCategoryRepository.addCacheCategory(data = createCacheCategoryData).run.run

          result must beLike {
            case Errata(e) => e.headOption must beSome.which {
              case (_, (_, repositoryException)) => repositoryException must beLike {
                case e: RepositoryException => e.cause must beSome.which(_ shouldEqual contentResolverException)
              }
            }
          }
        }
    }

    "deleteCacheCategory" should {

      "return a successful response when a valid cache category id is given" in
        new CacheCategoryRepositoryScope
          with ValidCacheCategoryRepositoryResponses {

          val result = cacheCategoryRepository.deleteCacheCategory(cacheCategory = cacheCategory).run.run

          result must beLike {
            case Answer(deleted) =>
              deleted shouldEqual 1
          }
        }

      "return a RepositoryException when a exception is thrown" in
        new CacheCategoryRepositoryScope
          with ErrorCacheCategoryRepositoryResponses {

          val result = cacheCategoryRepository.deleteCacheCategory(cacheCategory = cacheCategory).run.run

          result must beLike {
            case Errata(e) => e.headOption must beSome.which {
              case (_, (_, repositoryException)) => repositoryException must beLike {
                case e: RepositoryException => e.cause must beSome.which(_ shouldEqual contentResolverException)
              }
            }
          }
        }
    }

    "deleteCacheCategoryByPackage" should {

      "return a successful response when a valid package name is given" in
        new CacheCategoryRepositoryScope
          with ValidCacheCategoryRepositoryResponses {

          val result = cacheCategoryRepository.deleteCacheCategoryByPackage(packageName = testPackageName).run.run

          result must beLike {
            case Answer(deleted) =>
              deleted shouldEqual 1
          }
        }

      "return a RepositoryException when a exception is thrown" in
        new CacheCategoryRepositoryScope
          with ErrorCacheCategoryRepositoryResponses {

          val result = cacheCategoryRepository.deleteCacheCategoryByPackage(packageName = testPackageName).run.run

          result must beLike {
            case Errata(e) => e.headOption must beSome.which {
              case (_, (_, repositoryException)) => repositoryException must beLike {
                case e: RepositoryException => e.cause must beSome.which(_ shouldEqual contentResolverException)
              }
            }
          }
        }
    }

    "fetchCacheCategories" should {

      "return all the cache categories stored in the database" in
        new CacheCategoryRepositoryScope
          with ValidCacheCategoryRepositoryResponses {

          val result = cacheCategoryRepository.fetchCacheCategories.run.run

          result must beLike {
            case Answer(categories) =>
              categories shouldEqual cacheCategorySeq
          }
        }

      "return a RepositoryException when a exception is thrown" in
        new CacheCategoryRepositoryScope
          with ErrorCacheCategoryRepositoryResponses {

          val result = cacheCategoryRepository.fetchCacheCategories.run.run

          result must beLike {
            case Errata(e) => e.headOption must beSome.which {
              case (_, (_, repositoryException)) => repositoryException must beLike {
                case e: RepositoryException => e.cause must beSome.which(_ shouldEqual contentResolverException)
              }
            }
          }
        }
    }

    "findCacheCategoryById" should {

      "return a CacheCategory object when a existent id is given" in
        new CacheCategoryRepositoryScope
          with ValidCacheCategoryRepositoryResponses {

          val result = cacheCategoryRepository.findCacheCategoryById(id = testCacheCategoryId).run.run

          result must beLike {
            case Answer(maybeCacheCategory) =>
              maybeCacheCategory must beSome[CacheCategory].which { cacheCategory =>
                cacheCategory.id shouldEqual testCacheCategoryId
                cacheCategory.data.packageName shouldEqual testPackageName
              }
          }
        }

      "return None when a non-existent id is given" in
        new CacheCategoryRepositoryScope
          with ValidCacheCategoryRepositoryResponses {

          val result = cacheCategoryRepository.findCacheCategoryById(id = testNonExistingCacheCategoryId).run.run

          result must beLike {
            case Answer(maybeCacheCategory) =>
              maybeCacheCategory must beNone
          }
        }

      "return a RepositoryException when a exception is thrown" in
        new CacheCategoryRepositoryScope
          with ErrorCacheCategoryRepositoryResponses {

          val result = cacheCategoryRepository.findCacheCategoryById(id = testCacheCategoryId).run.run

          result must beLike {
            case Errata(e) => e.headOption must beSome.which {
              case (_, (_, repositoryException)) => repositoryException must beLike {
                case e: RepositoryException => e.cause must beSome.which(_ shouldEqual contentResolverException)
              }
            }
          }
        }
    }

    "fetchCacheCategoryByPackage" should {
      "return a CacheCategory object when a existent package name is given" in
        new CacheCategoryRepositoryScope
          with ValidCacheCategoryRepositoryResponses {

          val result = cacheCategoryRepository.fetchCacheCategoryByPackage(packageName = testPackageName).run.run

          result must beLike {
            case Answer(maybeCacheCategory) =>
              maybeCacheCategory must beSome[CacheCategory].which { cacheCategory =>
                cacheCategory.id shouldEqual testCacheCategoryId
                cacheCategory.data.packageName shouldEqual testPackageName
              }
          }
        }

      "return None when a non-existent package name is given" in
        new CacheCategoryRepositoryScope
          with ValidCacheCategoryRepositoryResponses {

          val result = cacheCategoryRepository.fetchCacheCategoryByPackage(packageName = testNonExistingPackageName).run.run

          result must beLike {
            case Answer(maybeCacheCategory) =>
              maybeCacheCategory must beNone
          }
        }

      "return a RepositoryException when a exception is thrown" in
        new CacheCategoryRepositoryScope
          with ErrorCacheCategoryRepositoryResponses {

          val result = cacheCategoryRepository.fetchCacheCategoryByPackage(packageName = testPackageName).run.run

          result must beLike {
            case Errata(e) => e.headOption must beSome.which {
              case (_, (_, repositoryException)) => repositoryException must beLike {
                case e: RepositoryException => e.cause must beSome.which(_ shouldEqual contentResolverException)
              }
            }
          }
        }
    }

    "updateCacheCategory" should {

      "return a successful response when the cache category is updated" in
        new CacheCategoryRepositoryScope
          with ValidCacheCategoryRepositoryResponses {

          val result = cacheCategoryRepository.updateCacheCategory(cacheCategory = cacheCategory).run.run

          result must beLike {
            case Answer(updated) =>
              updated shouldEqual 1
          }
        }

      "return a RepositoryException when a exception is thrown" in
        new CacheCategoryRepositoryScope
          with ErrorCacheCategoryRepositoryResponses {

          val result = cacheCategoryRepository.updateCacheCategory(cacheCategory = cacheCategory).run.run

          result must beLike {
            case Errata(e) => e.headOption must beSome.which {
              case (_, (_, repositoryException)) => repositoryException must beLike {
                case e: RepositoryException => e.cause must beSome.which(_ shouldEqual contentResolverException)
              }
            }
          }
        }
    }

    "getEntityFromCursor" should {

      "return None when an empty cursor is given" in
        new EmptyCacheCategoryMockCursor
          with CacheCategoryRepositoryScope {

          val result = getEntityFromCursor(cacheCategoryEntityFromCursor)(mockCursor)

          result must beNone
        }

      "return a CacheCategory object when a cursor with data is given" in
        new CacheCategoryMockCursor
          with CacheCategoryRepositoryScope {

          val result = getEntityFromCursor(cacheCategoryEntityFromCursor)(mockCursor)

          result must beSome[CacheCategoryEntity].which { cacheCategory =>
            cacheCategory.id shouldEqual cacheCategoryEntity.id
            cacheCategory.data shouldEqual cacheCategoryEntity.data
          }
        }
    }

    "getListFromCursor" should {

      "return an empty sequence when an empty cursor is given" in
        new EmptyCacheCategoryMockCursor
          with CacheCategoryRepositoryScope {

          val result = getListFromCursor(cacheCategoryEntityFromCursor)(mockCursor)

          result should beEmpty
        }

      "return a CacheCategory sequence when a cursor with data is given" in
        new CacheCategoryMockCursor
          with CacheCategoryRepositoryScope {
          val result = getListFromCursor(cacheCategoryEntityFromCursor)(mockCursor)

          result shouldEqual cacheCategoryEntitySeq
        }
    }
  }

}