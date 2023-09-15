package okinawa.flat_e.hello_jetpack_compose

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import okinawa.flat_e.hello_jetpack_compose.ui.theme.HelloJetpackComposeTheme
import okinawa.flat_e.hello_jetpack_compose.ui.theme.shapeScheme

/**
 * Jetpack Compose Tutorial
 * https://developer.android.com/jetpack/compose/tutorial
 *
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
//            HelloJetpackComposeTheme {
//                // A surface container using the 'background' color from the theme
//                Surface(
//                    modifier = Modifier.fillMaxSize(),
//                    color = MaterialTheme.colorScheme.background
//                ) {
//                    Greeting("Android")
//                }
//            }
            //Text("Hello world!")
            HelloJetpackComposeTheme {
                Surface( modifier = Modifier.fillMaxSize() ) {
                    //MessageCard(Message("Android", "Jetpack Compose"))
                    Conversation(messages = conversationSample)
                }
            }
        }
    }
}

val conversationSample = listOf(
    Message("Lexi","Hey, take a look at Jetpack Compose, it's great!"),
    Message("Lexi","Hey, take a look at Jetpack Compose, it's great!"),
    Message("Lexi","Hey, take a look at Jetpack Compose, it's great!"),
    Message("Lexi","Hey, take a look at Jetpack Compose, it's great!"),
    Message("Lexi","Hey, take a look at Jetpack Compose, it's great!"),
    Message("Lexi","Hey, take a look at Jetpack Compose, it's great!"),
)

data class Message(val author: String, val body: String)

@Composable
fun MessageCard(msg: Message) {
    Row(modifier = Modifier.padding(all = 8.dp)) {
        Image (
            painter = painterResource(R.drawable.logo_flat_e),
            contentDescription = "Contact profile picture",
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .border(1.5.dp, MaterialTheme.colorScheme.primary, CircleShape)
        )

        Spacer(modifier = Modifier.width(8.dp))

        var isExpanded by remember { mutableStateOf(false)}

        val surfaceColor by animateColorAsState(
            if (isExpanded) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface
        )

        Column(modifier = Modifier.clickable {isExpanded = !isExpanded}) {
            Text(
                text= msg.author,
                color = MaterialTheme.colorScheme.secondary,
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(modifier = Modifier.width(4.dp))
            Surface(
                shape = MaterialTheme.shapeScheme.large,
                shadowElevation = 1.dp,
                color = surfaceColor,
                modifier = Modifier.animateContentSize().padding(1.dp)
            ) {
                Text(
                    text = msg.body,
                    modifier = Modifier.padding(all = 4.dp),
                    style = MaterialTheme.typography.bodyLarge,
                    maxLines = if (isExpanded) {Int.MAX_VALUE} else {1},
                )
            }
        }
    }
}

@Preview(name = "Light mode")
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true,
    name = "Dark Mode"
)
@Composable
fun PreviewMessageCard() {
    HelloJetpackComposeTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            Message("Lexi","Hey, take a look at Jetpack Compose, it's great!")
        }
    }
}

@Composable
fun Conversation(messages: List<Message>) {
    LazyColumn {
        messages.map { item { MessageCard(it) }}
    }
}

@Preview
@Composable
fun PreviewConversation() {
    HelloJetpackComposeTheme {
        Conversation(conversationSample)
    }
}
