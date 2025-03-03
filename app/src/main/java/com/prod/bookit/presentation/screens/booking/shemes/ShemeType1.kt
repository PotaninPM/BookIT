package com.prod.bookit.presentation.screens.booking.shemes

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.prod.bookit.presentation.models.BookObject
import com.prod.bookit.domain.model.BookObjectUIData
import com.prod.bookit.presentation.models.CoworkingDefaults
import com.prod.bookit.presentation.screens.booking.objects.BookObjectDouble
import com.prod.bookit.presentation.screens.booking.objects.BookObjectQuad
import com.prod.bookit.presentation.screens.booking.objects.BookObjectSingle
import com.prod.bookit.presentation.screens.booking.objects.BookObjectTriple
import com.prod.bookit.presentation.screens.booking.objects.HorizontalWall
import com.prod.bookit.presentation.screens.booking.objects.VerticalWall
import com.prod.bookit.presentation.theme.DarkBlueTheme
import com.prod.bookit.presentation.theme.LightBlueTheme

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ShemeType1(
    modifier: Modifier = Modifier,
    onBookObjectClick: (BookObjectUIData) -> Unit = {},
    bookObjects: List<BookObjectUIData> = List(28) { BookObjectUIData(
        id = it.toString(),
        position = it + 1,
        avalibleToBook = false
    ) }
) {
    Column(
        modifier = modifier
    ) {
        HorizontalWall(modifier = Modifier.width((CoworkingDefaults.cellSize * 2 + CoworkingDefaults.spaceSize + 48.dp + CoworkingDefaults.wallWidth * 2) * 2 + 128.dp))

        Row {
            VerticalWall(modifier = Modifier.height(
                (CoworkingDefaults.cellSize * 2 + CoworkingDefaults.spaceSize + 48.dp) * 6 + 24.dp + CoworkingDefaults.wallWidth * 4
            ))

            Column {
                for (i in 0..3) {
                    FlowRow(
                        maxLines = 2,
                        verticalArrangement = Arrangement.spacedBy(CoworkingDefaults.spaceSize),
                        modifier = Modifier
                            .padding(24.dp)
                            .width(CoworkingDefaults.cellSize * 2 + CoworkingDefaults.spaceSize)
                    ) {
                        BookObjectSingle(
                            bookObjectUIData = bookObjects[i * 3],
                            onClick = { onBookObjectClick(bookObjects[i * 3]) }
                        )

                        Spacer(modifier = Modifier.width(CoworkingDefaults.spaceSize))

                        BookObjectSingle(
                            bookObjectUIData = bookObjects[i * 3 + 1],
                            onClick = { onBookObjectClick(bookObjects[i * 3 + 1]) }
                        )

                        BookObjectDouble(
                            bookObjectUIData = bookObjects[i * 3 + 2],
                            onClick = { onBookObjectClick(bookObjects[i * 3 + 2]) }
                        )
                    }

                    HorizontalWall(modifier = Modifier.width(CoworkingDefaults.cellSize * 2 + CoworkingDefaults.spaceSize + 48.dp))
                }

                BookObjectQuad(
                    modifier = Modifier.padding(24.dp).padding(top = 24.dp),
                    bookObjectUIData = bookObjects[24],
                    onClick = { onBookObjectClick(bookObjects[24]) }
                )

                BookObjectTriple(
                    modifier = Modifier.padding(24.dp),
                    bookObjectUIData = bookObjects[25],
                    onClick = { onBookObjectClick(bookObjects[25]) },
                    ltr = true
                )
            }

            VerticalWall(modifier = Modifier.height(
                (CoworkingDefaults.cellSize * 2 + CoworkingDefaults.spaceSize + 48.dp + CoworkingDefaults.wallWidth) * 4
            ))

            Spacer(modifier = Modifier.width(128.dp))

            VerticalWall(modifier = Modifier.height(
                (CoworkingDefaults.cellSize * 2 + CoworkingDefaults.spaceSize + 48.dp + CoworkingDefaults.wallWidth) * 4
            ))

            Column {
                for (i in 0..3) {
                    FlowRow(
                        maxLines = 2,
                        verticalArrangement = Arrangement.spacedBy(CoworkingDefaults.spaceSize),
                        modifier = Modifier
                            .padding(24.dp)
                            .width(CoworkingDefaults.cellSize * 2 + CoworkingDefaults.spaceSize)
                    ) {
                        BookObjectSingle(
                            bookObjectUIData = bookObjects[i * 3 + 12],
                            onClick = { onBookObjectClick(bookObjects[i * 3 + 12]) }
                        )

                        Spacer(modifier = Modifier.width(CoworkingDefaults.spaceSize))

                        BookObjectSingle(
                            bookObjectUIData = bookObjects[i * 3 + 13],
                            onClick = { onBookObjectClick(bookObjects[i * 3 + 13]) }
                        )

                        BookObjectDouble(
                            bookObjectUIData = bookObjects[i * 3 + 14],
                            onClick = { onBookObjectClick(bookObjects[i * 3 + 14]) }
                        )
                    }

                    HorizontalWall(modifier = Modifier.width(CoworkingDefaults.cellSize * 2 + CoworkingDefaults.spaceSize + 48.dp))
                }

                BookObjectQuad(
                    modifier = Modifier.padding(24.dp).padding(top = 24.dp),
                    bookObjectUIData = bookObjects[26],
                    onClick = { onBookObjectClick(bookObjects[26]) }
                )

                BookObjectTriple(
                    modifier = Modifier.padding(24.dp),
                    bookObjectUIData = bookObjects[27],
                    onClick = { onBookObjectClick(bookObjects[27]) },
                    ltr = false
                )
            }

            VerticalWall(modifier = Modifier.height(
                (CoworkingDefaults.cellSize * 2 + CoworkingDefaults.spaceSize + 48.dp) * 6 + 24.dp + CoworkingDefaults.wallWidth * 4
            ))
        }

        HorizontalWall(modifier = Modifier.width((CoworkingDefaults.cellSize * 2 + CoworkingDefaults.spaceSize + 48.dp + CoworkingDefaults.wallWidth * 2) * 2 + 128.dp))
    }
}


@Composable
private fun ShemeType1Preview() {
    Surface {
        ShemeType1()
    }
}

@Preview(device = "spec:width=500dp,height=2000dp,dpi=440")
@Composable
private fun ShemeType1DarkPreview() {
    MaterialTheme(
        colorScheme = DarkBlueTheme
    ) {
        ShemeType1Preview()
    }
}

@Preview(device = "spec:width=500dp,height=2000dp,dpi=440")
@Composable
private fun ShemeType1LightPreview() {
    MaterialTheme(
        colorScheme = LightBlueTheme
    ) {
        ShemeType1Preview()
    }
}