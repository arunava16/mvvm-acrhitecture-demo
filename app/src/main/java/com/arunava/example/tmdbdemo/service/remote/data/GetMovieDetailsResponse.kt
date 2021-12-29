package com.arunava.example.tmdbdemo.service.remote.data

import com.google.gson.annotations.SerializedName

data class GetMovieDetailsResponse(
    @SerializedName("adult") var adult: Boolean,
    @SerializedName("backdrop_path") var backdropPath: String,
    @SerializedName("belongs_to_collection") var belongsToCollection: BelongsToCollection,
    @SerializedName("budget") var budget: Int,
    @SerializedName("genres") var genres: List<Genres>,
    @SerializedName("homepage") var homepage: String,
    @SerializedName("id") var id: Int,
    @SerializedName("imdb_id") var imdbId: String,
    @SerializedName("original_language") var originalLanguage: String,
    @SerializedName("original_title") var originalTitle: String,
    @SerializedName("overview") var overview: String,
    @SerializedName("popularity") var popularity: Double,
    @SerializedName("poster_path") var posterPath: String,
    @SerializedName("production_companies") var productionCompanies: List<ProductionCompanies>,
    @SerializedName("production_countries") var productionCountries: List<ProductionCountries>,
    @SerializedName("release_date") var releaseDate: String,
    @SerializedName("revenue") var revenue: Int,
    @SerializedName("runtime") var runtime: Int,
    @SerializedName("spoken_languages") var spokenLanguages: List<SpokenLanguages>,
    @SerializedName("status") var status: String,
    @SerializedName("tagline") var tagline: String,
    @SerializedName("title") var title: String,
    @SerializedName("video") var video: Boolean,
    @SerializedName("vote_average") var voteAverage: Double,
    @SerializedName("vote_count") var voteCount: Int
)

data class BelongsToCollection(
    @SerializedName("id") var id: Int,
    @SerializedName("name") var name: String,
    @SerializedName("poster_path") var posterPath: String,
    @SerializedName("backdrop_path") var backdropPath: String
)

data class Genres(
    @SerializedName("id") var id: Int,
    @SerializedName("name") var name: String
)

data class ProductionCompanies(
    @SerializedName("name") var name: String,
    @SerializedName("id") var id: Int,
    @SerializedName("logo_path") var logoPath: String,
    @SerializedName("origin_country") var originCountry: String
)

data class ProductionCountries(
    @SerializedName("iso_3166_1") var iso31661: String,
    @SerializedName("name") var name: String
)

data class SpokenLanguages(
    @SerializedName("iso_639_1") var iso6391: String,
    @SerializedName("name") var name: String,
    @SerializedName("english_name") var englishName: String
)