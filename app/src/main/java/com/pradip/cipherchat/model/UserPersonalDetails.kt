package com.pradip.cipherchat.model

class UserPersonalDetails {
    var uid: String? = null
    var fullName: String? = null
    var phoneNumber: String? = null
    var profileImage: String? = null
    var bio: String? = null

    constructor() {}
    constructor(
        uid: String?,
        fullName: String?,
        phoneNumber: String?,
        profileImage: String?,
        bio: String?
    ) {
        this.uid = uid
        this.fullName = fullName
        this.phoneNumber = phoneNumber
        this.profileImage = profileImage
        this.bio = bio
    }
}