package com.coen268.tripmate

class PlacesKH {
    var name: String = ""
    var comment: String = ""
    var place:String=""
    var phoneNum:String=""
    var link:String=""

    constructor() {}

    constructor(name: String, link: String) {
        this.name = name
        this.link = link

    }
}