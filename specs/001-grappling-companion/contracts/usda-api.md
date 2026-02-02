# API Contract: USDA FoodData Central

**Feature**: Grappling Companion - Nutrition Tracking
**API**: USDA FoodData Central API v1
**Date**: 2026-01-28
**Phase**: 1 (Design & Contracts)

## Overview

The USDA FoodData Central API provides comprehensive nutrition data for foods. This contract defines the integration for food search functionality in the Grappling Companion app.

**Base URL**: `https://api.nal.usda.gov/fdc/v1/`

**Authentication**: API key required (query parameter)

**Rate Limits**:
- Free tier: 1000 requests/hour
- Sufficient for single-user mobile app usage pattern

**Official Documentation**: https://fdc.nal.usda.gov/api-guide.html

## Endpoints

### 1. Search Foods

Search for foods by name/description.

**Endpoint**: `GET /foods/search`

**Authentication**: API key in query parameter

**Request Parameters**:

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| api_key | string | Yes | Your API key |
| query | string | Yes | Search term (food name) |
| pageSize | integer | No | Number of results (default: 25, max: 200) |
| pageNumber | integer | No | Page number for pagination (default: 1) |
| dataType | string | No | Filter by data type (Foundation, SR Legacy, Survey) |

**Example Request**:
```
GET https://api.nal.usda.gov/fdc/v1/foods/search?api_key=YOUR_API_KEY&query=chicken%20breast&pageSize=25
```

**Response Format**: JSON

**Success Response** (HTTP 200):

```json
{
  "totalHits": 532,
  "currentPage": 1,
  "totalPages": 22,
  "pageList": [1, 2, 3, 4, 5],
  "foodSearchCriteria": {
    "query": "chicken breast",
    "pageSize": 25,
    "pageNumber": 1
  },
  "foods": [
    {
      "fdcId": 171477,
      "description": "Chicken, broilers or fryers, breast, meat only, raw",
      "dataType": "SR Legacy",
      "publicationDate": "2019-04-01",
      "brandOwner": "",
      "gtinUpc": "",
      "ingredients": "",
      "foodNutrients": [
        {
          "nutrientId": 1008,
          "nutrientName": "Energy",
          "nutrientNumber": "208",
          "unitName": "KCAL",
          "derivationCode": "A",
          "derivationDescription": "Analytical",
          "value": 120
        },
        {
          "nutrientId": 1003,
          "nutrientName": "Protein",
          "nutrientNumber": "203",
          "unitName": "G",
          "value": 22.5
        },
        {
          "nutrientId": 1004,
          "nutrientName": "Total lipid (fat)",
          "nutrientNumber": "204",
          "unitName": "G",
          "value": 2.62
        },
        {
          "nutrientId": 1005,
          "nutrientName": "Carbohydrate, by difference",
          "nutrientNumber": "205",
          "unitName": "G",
          "value": 0
        }
      ]
    }
  ]
}
```

**Error Responses**:

**401 Unauthorized** (Invalid API key):
```json
{
  "error": {
    "code": "API_KEY_MISSING",
    "message": "An API key is required for this request"
  }
}
```

**429 Too Many Requests** (Rate limit exceeded):
```json
{
  "error": {
    "code": "OVER_RATE_LIMIT",
    "message": "Rate limit exceeded. Try again later."
  }
}
```

**500 Internal Server Error**:
```json
{
  "error": {
    "code": "INTERNAL_ERROR",
    "message": "An internal error occurred"
  }
}
```

## Key Nutrient IDs

The app requires these specific nutrients for macro tracking:

| Nutrient ID | Nutrient Name | Unit | Purpose |
|-------------|---------------|------|---------|
| 1008 | Energy | KCAL | Calories |
| 1003 | Protein | G | Protein macros |
| 1004 | Total lipid (fat) | G | Fat macros |
| 1005 | Carbohydrate, by difference | G | Carb macros |

**Note**: Nutrient values are per 100g of food.

## Data Model Mapping

### DTO (Data Transfer Object)

```kotlin
@Serializable
data class FoodSearchResponse(
    val totalHits: Int,
    val currentPage: Int,
    val totalPages: Int,
    val foods: List<FoodDto>
)

@Serializable
data class FoodDto(
    val fdcId: Int,
    val description: String,
    val dataType: String,
    val foodNutrients: List<FoodNutrientDto>
)

@Serializable
data class FoodNutrientDto(
    val nutrientId: Int,
    val nutrientName: String,
    val unitName: String,
    val value: Float
)
```

### Domain Model Conversion

```kotlin
fun FoodDto.toDomain(): Food {
    return Food(
        fdcId = fdcId,
        name = description,
        caloriesPer100g = getNutrientValue(1008) ?: 0f,
        proteinPer100g = getNutrientValue(1003) ?: 0f,
        fatPer100g = getNutrientValue(1004) ?: 0f,
        carbsPer100g = getNutrientValue(1005) ?: 0f,
        isCustom = false
    )
}

private fun FoodDto.getNutrientValue(nutrientId: Int): Float? {
    return foodNutrients
        .firstOrNull { it.nutrientId == nutrientId }
        ?.value
}
```

## Integration Requirements

### Retrofit Interface

```kotlin
interface UsdaFoodApi {
    @GET("foods/search")
    suspend fun searchFoods(
        @Query("api_key") apiKey: String,
        @Query("query") query: String,
        @Query("pageSize") pageSize: Int = 25
    ): FoodSearchResponse
}
```

### Network Configuration

**Retrofit Setup**:
- Base URL: `https://api.nal.usda.gov/fdc/v1/`
- Converter: Kotlin Serialization JSON
- Timeout: 10 seconds (read), 5 seconds (connect)
- Logging: OkHttp logging interceptor (debug builds only)

**Error Handling**:
```kotlin
sealed class ApiResult<out T> {
    data class Success<T>(val data: T) : ApiResult<T>()
    data class Error(val message: String, val code: Int? = null) : ApiResult<Nothing>()
    object NetworkError : ApiResult<Nothing>()
}
```

### Caching Strategy

**Local Cache** (Room database):
- Cache successful search results
- TTL: 7 days (nutrition data rarely changes)
- Cache key: Search query string (normalized, lowercase)

**Cache-First Flow**:
1. User searches for "chicken"
2. Check local cache for "chicken"
3. If found and not expired → return cached results
4. If not found or expired → call API, cache response, return results
5. If API fails → return cached results with warning message

**Benefits**:
- Reduced API calls (stay under rate limit)
- Offline access to previously searched foods
- Faster response time for repeated searches

### Repository Implementation

```kotlin
class NutritionRepositoryImpl(
    private val api: UsdaFoodApi,
    private val foodCacheDao: FoodCacheDao,
    private val apiKey: String
) : NutritionRepository {

    override suspend fun searchFoods(query: String): Result<List<Food>> {
        // 1. Check cache first
        val cached = foodCacheDao.getCachedSearch(query.lowercase())
        if (cached != null && !cached.isExpired()) {
            return Result.Success(cached.foods.map { it.toDomain() })
        }

        // 2. Call API
        return try {
            val response = api.searchFoods(
                apiKey = apiKey,
                query = query,
                pageSize = 25
            )

            val foods = response.foods.map { it.toDomain() }

            // 3. Cache results
            foodCacheDao.cacheSearch(
                query = query.lowercase(),
                foods = foods.map { it.toEntity() },
                timestamp = System.currentTimeMillis()
            )

            Result.Success(foods)
        } catch (e: HttpException) {
            when (e.code()) {
                401 -> Result.Error("Invalid API key")
                429 -> Result.Error("Rate limit exceeded. Try again later.")
                else -> Result.Error("Failed to search foods: ${e.message()}")
            }
        } catch (e: IOException) {
            // Network error - return cached results if available
            if (cached != null) {
                Result.Success(
                    data = cached.foods.map { it.toDomain() },
                    message = "Showing cached results (offline)"
                )
            } else {
                Result.Error("No internet connection and no cached results")
            }
        }
    }
}
```

## Usage Patterns

### Typical User Flow

1. **User Action**: Enters "chicken breast" in food search
2. **App Behavior**:
   - Debounce input (wait 500ms after last keystroke)
   - Call `searchFoods("chicken breast")`
   - Show loading indicator
3. **Network Response**:
   - Success → Display list of foods
   - Error → Show error message + cached results if available
4. **User Selection**: User taps a food from results
5. **Add to Meal**: User specifies portion size (grams) and meal type
6. **Calculate Nutrition**: App calculates macros based on portion
7. **Save**: Create FoodLog entry in local database

### Error Scenarios & User Experience

| Error | User Experience |
|-------|-----------------|
| No internet + no cache | Show message: "No internet connection. Search requires internet for first-time foods." |
| No internet + has cache | Show cached results with banner: "Showing saved results (offline)" |
| Rate limit exceeded | Show message: "Too many searches. Please wait a moment and try again." |
| API down (500 error) | Show cached results if available, otherwise: "Food search temporarily unavailable. Try again later." |
| No results found | Show: "No foods found for '{query}'. Try a different search term." |

## Security & Best Practices

### API Key Management

**Storage**:
- API key stored in `local.properties` (not committed to git)
- Injected at build time into `BuildConfig`
- Never hardcoded or logged

**Example** (`local.properties`):
```properties
USDA_API_KEY=your_api_key_here
```

**Build Configuration** (`build.gradle.kts`):
```kotlin
buildConfigField("String", "USDA_API_KEY", "\"${project.properties["USDA_API_KEY"]}\"")
```

**Usage in Code**:
```kotlin
@Provides
@Singleton
fun provideUsdaApiKey(): String = BuildConfig.USDA_API_KEY
```

### Request Optimization

**Debouncing**:
- Wait 500ms after last keystroke before searching
- Prevents excessive API calls during typing

**Minimum Query Length**:
- Require at least 2 characters before searching
- Reduces meaningless API calls

**Pagination**:
- Request only 25 results per page (sufficient for mobile UX)
- Load more on scroll if needed (future enhancement)

### Data Privacy

- No user data sent to USDA API
- Search terms are generic food names (no personal info)
- All nutrition data is public domain

## Testing Strategy

### Contract Tests

```kotlin
@Test
fun `USDA API search returns valid response`() = runTest {
    val api = retrofit.create(UsdaFoodApi::class.java)

    val response = api.searchFoods(
        apiKey = testApiKey,
        query = "apple",
        pageSize = 5
    )

    assertThat(response.foods).isNotEmpty()
    assertThat(response.foods[0].fdcId).isGreaterThan(0)
    assertThat(response.foods[0].description).contains("apple", ignoreCase = true)
    assertThat(response.foods[0].foodNutrients).isNotEmpty()
}

@Test
fun `USDA API handles invalid API key`() = runTest {
    val api = retrofit.create(UsdaFoodApi::class.java)

    assertThrows<HttpException> {
        api.searchFoods(
            apiKey = "invalid_key",
            query = "apple",
            pageSize = 5
        )
    }.also { exception ->
        assertThat(exception.code()).isEqualTo(401)
    }
}
```

### Mock Responses (for unit tests)

```kotlin
val mockFoodSearchResponse = FoodSearchResponse(
    totalHits = 1,
    currentPage = 1,
    totalPages = 1,
    foods = listOf(
        FoodDto(
            fdcId = 123456,
            description = "Chicken, breast, raw",
            dataType = "SR Legacy",
            foodNutrients = listOf(
                FoodNutrientDto(1008, "Energy", "KCAL", 120f),
                FoodNutrientDto(1003, "Protein", "G", 22.5f),
                FoodNutrientDto(1004, "Total lipid (fat)", "G", 2.6f),
                FoodNutrientDto(1005, "Carbohydrate", "G", 0f)
            )
        )
    )
)
```

## API Key Acquisition

### For Developers

1. Visit: https://fdc.nal.usda.gov/api-key-signup.html
2. Fill out signup form (name, email, organization)
3. Receive API key via email (instant)
4. Add to `local.properties`:
   ```
   USDA_API_KEY=your_key_here
   ```

### For Production

- Same API key can be used for production
- Monitor usage to ensure staying under rate limits
- Consider requesting higher limits if app scales beyond single-user usage

## Summary

This contract defines:
- ✅ USDA FoodData Central API integration for food search
- ✅ Request/response formats with example data
- ✅ Error handling strategy
- ✅ Caching strategy for offline support
- ✅ Security best practices (API key management)
- ✅ User experience considerations for various scenarios
- ✅ Testing approach
