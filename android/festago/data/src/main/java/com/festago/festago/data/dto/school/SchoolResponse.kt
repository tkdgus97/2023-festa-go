package com.festago.festago.data.dto.school

import com.festago.festago.domain.model.school.School

data class SchoolResponse(
    val id: Long,
    val name: String,
    val imageUrl: String,
) {
    fun toDomain() = School(
        id = id,
        name = name,
        imageUrl = imageUrl,
    )
}