package com.festago.festago.presentation.ui.home.bookmarklist.schoolbookmark

sealed interface SchoolBookmarkEvent {
    class ShowSchoolDetail(val schoolId: Long) : SchoolBookmarkEvent
}