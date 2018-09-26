package com.evastos.movies.ui.movie.overview.adapter.suggestions

import android.app.SearchManager
import android.content.Context
import android.database.MatrixCursor
import android.provider.BaseColumns
import android.support.v4.widget.SimpleCursorAdapter
import com.evastos.movies.R
import com.evastos.movies.data.model.moviedb.Movie

/**
 * Shows movie suggestions while typing the query in the search view.
 */
class MovieSuggestionsAdapter(
    context: Context?
) : SimpleCursorAdapter(
    context,
    R.layout.layout_item_movie_suggestion,
    null,
    Array(1) { SearchManager.SUGGEST_COLUMN_TEXT_1 },
    IntArray(1) { R.id.movieSuggestionTextView },
    0
) {
    private var suggestions: List<Movie>? = null

    /**
     * Sets the movie suggestions.
     * Ugly solution but SearchView only takes an adapter that is a CursosAdapter.
     */
    fun setSuggestions(suggestions: List<Movie>) {
        this.suggestions = suggestions
        val columns = arrayOf(BaseColumns._ID, SearchManager.SUGGEST_COLUMN_TEXT_1)
        val cursor = MatrixCursor(columns)
        for (i in 0 until suggestions.size) {
            val row = arrayOf(i.toString(), suggestions[i].title)
            cursor.addRow(row)
        }
        swapCursor(cursor)
    }

    /**
     * Returns the movie title for the position, or null if there is no item at this position.
     */
    fun getTitle(position: Int): String? {
        suggestions?.let {
            if (position < it.size) {
                return it[position].title
            }
        }
        return null
    }
}
