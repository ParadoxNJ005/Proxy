package Activity

import Adapter.MessageAdapter
import DataClass.Message
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.proxy.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class chatting_Activity : AppCompatActivity() {

    private lateinit var chatRecyclerView: RecyclerView
    private lateinit var messageBox:EditText
    private lateinit var sendButton: ImageView
    private lateinit var receiverName: TextView
    private lateinit var messageList: ArrayList<Message>
    private lateinit var messageAdapter: MessageAdapter
    private lateinit var mdbRef: DatabaseReference
    private lateinit var receiverImg: ImageView

    private var receiverRoom:String?= null
    private var senderRoom: String?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chatting)

        val name = intent.getStringExtra("name")
        val receiveruid = intent.getStringExtra("uid")


        receiverName = findViewById(R.id.titleBar)
        receiverName.text = name.toString()

        val senderUid = FirebaseAuth.getInstance().currentUser?.uid
        mdbRef = FirebaseDatabase.getInstance().reference

        senderRoom = receiveruid + senderUid
        receiverRoom = senderUid + receiveruid

        chatRecyclerView = findViewById(R.id.chatRv)
        messageBox = findViewById(R.id.messageBox)
        sendButton = findViewById(R.id.sentBtn)
        messageList = ArrayList()
        messageAdapter = MessageAdapter(this, messageList)
        receiverImg = findViewById(R.id.img)

        Glide.with(this)
            .load(intent.getStringExtra("image"))
            .thumbnail(0.1f)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(receiverImg)

        chatRecyclerView.layoutManager = LinearLayoutManager(this)
        chatRecyclerView.adapter = messageAdapter

        // Check if "chats" node exists, if not, create it
        mdbRef.child("chats").addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                if (!snapshot.exists()) {
                    // "chats" node doesn't exist, create it
                    mdbRef.child("chats").setValue(true)
                        .addOnSuccessListener {
                            // Chats node created successfully
                        }
                        .addOnFailureListener { error ->
                            // Handle failure to create node
                            Toast.makeText(this@chatting_Activity, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
                        }
                }else{
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle onCancelled
                Toast.makeText(this@chatting_Activity, "Database Error: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })

        //adding data to recyclerView
        mdbRef.child("chats").child(senderRoom!!).child("messages")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    messageList.clear()
                    for (postSnapshot in snapshot.children) {
                        val message = postSnapshot.getValue(Message::class.java)
                        messageList.add(message!!)
                    }
                    messageAdapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle onCancelled
                    Toast.makeText(this@chatting_Activity, "Database Error: ${error.message}", Toast.LENGTH_SHORT).show()
                }
            })

// Adding the message to the database
        sendButton.setOnClickListener {
            val message = messageBox.text.toString()
            val messageObject = Message(message, senderUid)

            val senderMessageRef = mdbRef.child("chats").child(senderRoom!!).child("messages").push()
            senderMessageRef.setValue(messageObject)
                .addOnSuccessListener{

                        // Message sent successfully to sender's chat
                        Log.d("Firebase", "Message Sent Successfully to sender")
                        Toast.makeText(this@chatting_Activity, "Message Sent", Toast.LENGTH_SHORT).show()

                        val receiverMessageRef = mdbRef.child("chats").child(receiverRoom!!).child("messages").push()
                        receiverMessageRef.setValue(messageObject)
                            .addOnCompleteListener { receiverTask ->
                                if (receiverTask.isSuccessful) {
                                    // Message sent successfully to receiver's chat
                                    Log.d("Firebase", "Message Sent Successfully to receiver")
                                } else {

                                    Log.e("Firebase", "Error sending message to receiver: ${receiverTask.exception?.message}", receiverTask.exception)
                                    Toast.makeText(this@chatting_Activity, "Error sending message to receiver", Toast.LENGTH_SHORT).show()
                                }

                    }.addOnFailureListener{
                        Toast.makeText(this@chatting_Activity, "Error sending message", Toast.LENGTH_SHORT).show()
                    }
                }

            messageBox.setText("")
        }

    }

}