package ci.nsu.moble.main

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ci.nsu.moble.main.ui.theme.PracticeTheme

// Структура данных для хранения цветов
data class ColorItem(
    val name: String,
    val color: Color
)

class MainActivity : ComponentActivity() {

    // Структура для хранения доступных цветов
    private val availableColors = listOf(
        ColorItem("Red", Color.Red),
        ColorItem("Blue", Color.Blue),
        ColorItem("Green", Color.Green),
        ColorItem("Yellow", Color.Yellow),
        ColorItem("Cyan", Color.Cyan),
        ColorItem("Magenta", Color.Magenta),
        ColorItem("Black", Color.Black),
        ColorItem("White", Color.White),
        ColorItem("Gray", Color.Gray),
        ColorItem("DarkGray", Color.DarkGray),
        ColorItem("LightGray", Color.LightGray)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PracticeTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    ColorSearchScreen(
                        availableColors = availableColors,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun ColorSearchScreen(
    availableColors: List<ColorItem>,
    modifier: Modifier = Modifier
) {
    var searchQuery by remember { mutableStateOf("") }
    var buttonBackgroundColor by remember { mutableStateOf<Color?>(null) }
    var searchPerformed by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Поле ввода
        OutlinedTextField(
            value = searchQuery,
            onValueChange = {
                searchQuery = it
                searchPerformed = false
                buttonBackgroundColor = null
            },
            label = { Text("Введите название цвета") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Кнопка поиска
        Button(
            onClick = {
                searchPerformed = true
                val foundColor = availableColors.find {
                    it.name.equals(searchQuery, ignoreCase = true)
                }

                if (foundColor != null) {
                    buttonBackgroundColor = foundColor.color
                    Log.d("ColorSearch", "Цвет \"$searchQuery\" найден!")
                } else {
                    buttonBackgroundColor = null
                    Log.d("ColorSearch", "Пользовательский цвет \"$searchQuery\" не найден")
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (buttonBackgroundColor != null && searchPerformed) {
                    buttonBackgroundColor!!
                } else {
                    MaterialTheme.colorScheme.primary
                }
            )
        ) {
            Text("Найти цвет")
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Заголовок для палитры
        Text(
            text = "Палитра цветов:",
            fontSize = 20.sp,
            modifier = Modifier.align(Alignment.Start)
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Список палитры цветов
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(availableColors) { colorItem ->
                ColorPaletteItem(colorItem)
            }
        }
    }
}

@Composable
fun ColorPaletteItem(colorItem: ColorItem) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .background(
                color = colorItem.color,
                shape = RoundedCornerShape(8.dp)
            )
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = colorItem.name,
            color = getContrastColor(colorItem.color),
            modifier = Modifier.weight(1f),
            fontSize = 16.sp
        )
    }
}

// Функция для определения контрастного цвета текста
fun getContrastColor(color: Color): Color {
    // Вычисляем яркость цвета
    val brightness = (color.red * 299 + color.green * 587 + color.blue * 114) / 1000
    return if (brightness > 0.5) Color.Black else Color.White
}

@Preview(showBackground = true)
@Composable
fun ColorSearchScreenPreview() {
    PracticeTheme {
        val previewColors = listOf(
            ColorItem("Red", Color.Red),
            ColorItem("Blue", Color.Blue),
            ColorItem("Green", Color.Green)
        )
        ColorSearchScreen(availableColors = previewColors)
    }
}