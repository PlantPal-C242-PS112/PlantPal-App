package com.android.plantpal.data.remote

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.android.plantpal.data.remote.response.ListItemDiscussions
import com.android.plantpal.data.remote.retrofit.ApiService

class DiscussionPagingSource (private val apiService: ApiService) : PagingSource<Int, ListItemDiscussions>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ListItemDiscussions> {
        return try {
            val position = params.key ?: INITIAL_PAGE_INDEX
            val responseData = apiService.getAllDiscussions(position, params.loadSize)
            LoadResult.Page(
                data = responseData.data,
                prevKey = if (position == INITIAL_PAGE_INDEX) null else position - 1,
                nextKey = if (responseData.data.isEmpty()) null else position + 1
            )
        } catch (exception: Exception) {
            return LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, ListItemDiscussions>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }
}