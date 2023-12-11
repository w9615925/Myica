package uk.ac.tees.mad.w9615925

import android.content.Context
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

class Sounds : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val petSounds = listOf("Cat 1", "Cat 2", "Cat 3","Cat 4","Dog 1","Dog 2","Wolf")

            PetSoundsList(petSounds = petSounds) { sound ->
                playSound(this, sound)
            }
        }
    }

    @Composable
    fun PetSoundsList(petSounds: List<String>, onSoundClick: (String) -> Unit) {
        LazyColumn {

            items(petSounds) { sound ->
                ListItem(sound = sound, onSoundClick = onSoundClick)
            }
        }
    }

    @Composable
    fun ListItem(sound: String, onSoundClick: (String) -> Unit) {
        val imageResId = when (sound) {
            "Cat 1" -> R.drawable.sounds
            "Cat 2" -> R.drawable.sounds
            "Cat 3" ->R.drawable.sounds
            "Cat 4" -> R.drawable.sounds
            "Dog 1" -> R.drawable.sounds
            "Dog 2" ->R.drawable.sounds
            "Wolf" ->R.drawable.sounds

            else -> R.drawable.sounds
        }
        val image: Painter = painterResource(id = imageResId)

        Row(modifier = Modifier
            .fillMaxWidth()
            .clickable { onSoundClick(sound) }
            .padding(16.dp)) {
            Image(
                painter = image,
                contentDescription = null,
                modifier = Modifier.size(60.dp),
                contentScale = ContentScale.Crop
            )
            Text(
                text = sound,
                modifier = Modifier.padding(start = 20.dp)
            )
        }
    }



    fun playSound(context: Context, soundName: String) {
        val soundResId = when (soundName) {
            "Cat 1" -> R.raw.catmone
            "Cat 2" -> R.raw.catmeowtwo
            "Cat 3" ->R.raw.catpurone
            "Cat 4" -> R.raw.catpurtwo
            "Dog 1" -> R.raw.dogbark
            "Dog 2" ->R.raw.dogbarktwo
            "Wolf" ->R.raw.wolf
            else -> return
        }

        val mediaPlayer = MediaPlayer.create(context, soundResId)
        mediaPlayer.start()
        mediaPlayer.setOnCompletionListener { mp -> mp.release() }
    }

}