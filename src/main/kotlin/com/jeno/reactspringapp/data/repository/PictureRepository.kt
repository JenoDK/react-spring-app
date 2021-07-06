package com.jeno.reactspringapp.data.repository

import com.jeno.reactspringapp.data.model.Picture
import org.springframework.data.mongodb.repository.MongoRepository

interface PictureRepository : MongoRepository<Picture, String> {

}