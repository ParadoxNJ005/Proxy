package DataClass

class Message {
    var messages: String? = null
    var senderId: String? = null

    constructor(){}

    constructor(messages: String?, senderId: String?){
        this.messages = messages
        this.senderId = senderId
    }
}