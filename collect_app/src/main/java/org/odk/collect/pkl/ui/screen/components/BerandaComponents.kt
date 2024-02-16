package org.odk.collect.pkl.ui.screen.components

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.polstat.pkl.R
import com.polstat.pkl.database.entity.AnggotaTimEntity
import com.polstat.pkl.database.entity.SampelRutaEntity
import com.polstat.pkl.database.entity.WilayahEntity
import com.polstat.pkl.model.domain.Session
import com.polstat.pkl.ui.theme.PklBase
import com.polstat.pkl.ui.theme.PklPrimary100
import com.polstat.pkl.ui.theme.PklPrimary900
import com.polstat.pkl.ui.theme.PoppinsFontFamily
import com.polstat.pkl.utils.UtilFunctions
import org.odk.collect.pkl.ui.screen.DetailCard
import org.odk.collect.pkl.ui.screen.DetailRutaTextField
import java.util.Locale

@Composable
fun ProfileCard(
    session: Session
) {
    Card(
        modifier = Modifier
            .padding(16.dp)
            .shadow(8.dp)
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color.White,
            contentColor = Color.Gray,
        ),
        border = BorderStroke(1.dp, Color.White),
        shape = MaterialTheme.shapes.medium
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.AccountCircle,
                contentDescription = "Profile",
                tint = PklPrimary900,
                modifier = Modifier
                    .fillMaxWidth()
                    .size(80.dp)
                    .align(Alignment.CenterHorizontally)
            )
            Text(
                text = session.nama,
                modifier = Modifier.align(Alignment.CenterHorizontally),
                fontFamily = PoppinsFontFamily,
                fontWeight = FontWeight.Medium
            )
            Row(
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text(
                    text = session.nim,
                    fontFamily = PoppinsFontFamily,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = "   |   ",
                    fontFamily = PoppinsFontFamily,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = if (session.isKoor) "PML" else "PPL",
                    fontFamily = PoppinsFontFamily,
                    fontWeight = FontWeight.Medium
                )
            }
            Row(
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text(
                    text = session.namaTim,
                    fontFamily = PoppinsFontFamily,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = " - ",
                    fontFamily = PoppinsFontFamily,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = session.idTim,
                    fontFamily = PoppinsFontFamily,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
fun PmlCard(
    session: Session,
    context: Context
) {
    var hubungiPml by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .padding(16.dp)
            .shadow(8.dp)
            .fillMaxWidth()
            .clickable { hubungiPml = true },
        colors = CardDefaults.cardColors(
            containerColor = PklPrimary900,
            contentColor = Color.Gray,
        ),
        shape = MaterialTheme.shapes.medium
    ) {
        Column {
            Text(
                text = "PML".uppercase(),
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(8.dp),
                fontFamily = PoppinsFontFamily,
                fontWeight = FontWeight.Medium,
                fontSize = 20.sp,
                color = Color.White
            )
            Box(
                modifier = Modifier
                    .background(Color.White)
                    .fillMaxWidth()
                    .padding(12.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = session.namaPml,
                        fontFamily = PoppinsFontFamily,
                        fontWeight = FontWeight.Medium,
                        fontSize = 18.sp,
                        color = Color.DarkGray
                    )
                    Text(
                        text = session.nimPml,
                        fontFamily = PoppinsFontFamily,
                        fontWeight = FontWeight.Medium,
                        fontSize = 14.sp
                    )
                }
            }
        }

        if (hubungiPml) {
            // Intent to open WhatsApp
            Dialog(
                onDismissRequest = { hubungiPml = false },
                content = {
                    Column(
                        modifier = Modifier
                            .background(
                                color = Color.White,
                                shape = RoundedCornerShape(15.dp)
                            )
                            .height(250.dp),
                        Arrangement.Center,
                        Alignment.CenterHorizontally
                    ) {
                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(
                                    color = PklPrimary900,
                                    shape = RoundedCornerShape(
                                        topStart = 15.dp,
                                        topEnd = 15.dp
                                    )
                                )
                                .padding(
                                    top = 10.dp,
                                    bottom = 10.dp
                                ),
                            text = stringResource(id = R.string.hubungi_pml).uppercase(),
                            textAlign = TextAlign.Center,
                            color = Color.White,
                            fontFamily = PoppinsFontFamily,
                            fontWeight = FontWeight.Medium,
                            fontSize = 20.sp
                        )
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "${session.namaPml}".uppercase(),
                                fontSize = 20.sp,
                                textAlign = TextAlign.Center,
                                fontWeight = FontWeight.Medium,
                                color = Color.Black
                            )
                            Text(
                                text = "${session.noTlpPml}",
                                fontSize = 20.sp,
                                textAlign = TextAlign.Center,
                                fontWeight = FontWeight.Medium,
                                color = Color.Black
                            )
                            Divider(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(
                                        top = 10.dp,
                                        bottom = 10.dp
                                    )
                            )
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.clickable {
                                    openWhatsAppViaIntent(session.noTlpPml, context)
                                }
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_whatsapp),
                                    contentDescription = "WhatsApp Logo",
                                    modifier = Modifier
                                        .size(50.dp)
                                        .padding(start = 10.dp),
                                    tint = Color(0xFF25CF43)
                                )
                                Text(
                                    text = "Hubungi via WhatsApp",
                                    fontSize = 20.sp,
                                    textAlign = TextAlign.Center,
                                    color = Color.Black,
                                    fontWeight = FontWeight.Medium,
                                    modifier = Modifier
                                        .padding(start = 10.dp)
                                )
                            }
                        }

                        Text(modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                color = PklPrimary900,
                                shape = RoundedCornerShape(
                                    bottomStart = 15.dp,
                                    bottomEnd = 15.dp
                                )
                            )
                            .clickable { hubungiPml = false }
                            .padding(
                                top = 10.dp,
                                bottom = 10.dp
                            ),
                            textAlign = TextAlign.Center,
                            text = stringResource(id = R.string.close_popup_list_ruta).uppercase(),
                            color = Color.White,
                            fontFamily = PoppinsFontFamily,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Medium)
                    }
                }
            )
        }
    }
}

@Composable
fun ListPplCard(
    listAnggotaTim: List<AnggotaTimEntity>,
    context: Context
) {
    var selectedItemIndex by remember { mutableStateOf(-1) }

    Card(
        modifier = Modifier
            .padding(16.dp)
            .shadow(8.dp)
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = PklPrimary900,
            contentColor = Color.Gray,
        ),
        shape = MaterialTheme.shapes.medium
    ) {
        Column {
            Text(
                text = "Anggota Tim".uppercase(),
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(8.dp),
                fontFamily = PoppinsFontFamily,
                fontWeight = FontWeight.Medium,
                fontSize = 20.sp,
                color = Color.White
            )
            Box(
                modifier = Modifier
                    .background(Color.White)
                    .fillMaxWidth()
                    .padding(12.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    listAnggotaTim.forEachIndexed { index, anggotaTimEntity ->
                        Box(
                            modifier = Modifier
                                .clickable { selectedItemIndex = index }
                                .fillMaxWidth()
                        ) {
                            Column {
                                Text(
                                    text = anggotaTimEntity.nama,
                                    style = TextStyle(
                                        fontFamily = PoppinsFontFamily,
                                        fontWeight = FontWeight.Medium,
                                        fontSize = 18.sp,
                                        platformStyle = PlatformTextStyle(
                                            includeFontPadding = false
                                        )
                                    ),
                                    color = Color.DarkGray
                                )
                                Text(
                                    text = anggotaTimEntity.nim,
                                    style = TextStyle(
                                        fontFamily = PoppinsFontFamily,
                                        fontWeight = FontWeight.Medium,
                                        fontSize = 12.sp,
                                        platformStyle = PlatformTextStyle(
                                            includeFontPadding = false
                                        )
                                    )
                                )
                                Spacer(modifier = Modifier.height(12.dp))
                            }
                        }
                    }
                    selectedItemIndex.takeIf { it != -1 }?.let { index ->
                        Dialog(
                            onDismissRequest = { selectedItemIndex = -1 },
                            content = {
                                val selectedAnggota = listAnggotaTim[index]
                                Column(
                                    modifier = Modifier
                                        .background(
                                            color = Color.White,
                                            shape = RoundedCornerShape(15.dp)
                                        )
                                        .height(250.dp),
                                    verticalArrangement = Arrangement.Center,
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .background(
                                                color = PklPrimary900,
                                                shape = RoundedCornerShape(
                                                    topStart = 15.dp,
                                                    topEnd = 15.dp
                                                )
                                            )
                                            .padding(
                                                top = 10.dp,
                                                bottom = 10.dp
                                            ),
                                        text = stringResource(id = R.string.hubungi_ppl).uppercase(),
                                        textAlign = TextAlign.Center,
                                        color = Color.White,
                                        fontFamily = PoppinsFontFamily,
                                        fontWeight = FontWeight.Medium,
                                        fontSize = 20.sp
                                    )
                                    Column(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .weight(1f),
                                        verticalArrangement = Arrangement.Center,
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Text(
                                            text = "${selectedAnggota.nama}".uppercase(),
                                            fontSize = 20.sp,
                                            textAlign = TextAlign.Center,
                                            fontWeight = FontWeight.Medium,
                                            color = Color.Black
                                        )
                                        Text(
                                            text = "${selectedAnggota.noTlp}",
                                            fontSize = 20.sp,
                                            textAlign = TextAlign.Center,
                                            fontWeight = FontWeight.Medium,
                                            color = Color.Black
                                        )
                                        Divider(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(
                                                    top = 10.dp,
                                                    bottom = 10.dp
                                                )
                                        )
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            modifier = Modifier.clickable {
                                                openWhatsAppViaIntent(
                                                    selectedAnggota.noTlp,
                                                    context
                                                )
                                            }
                                        ) {
                                            Icon(
                                                painter = painterResource(id = R.drawable.ic_whatsapp),
                                                contentDescription = "WhatsApp Logo",
                                                modifier = Modifier
                                                    .size(50.dp)
                                                    .padding(start = 10.dp),
                                                tint = Color(0xFF25CF43)
                                            )
                                            Text(
                                                text = "Hubungi via WhatsApp",
                                                fontSize = 20.sp,
                                                textAlign = TextAlign.Center,
                                                color = Color.Black,
                                                fontWeight = FontWeight.Medium,
                                                modifier = Modifier.padding(start = 10.dp)
                                            )
                                        }
                                    }

                                    Text(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .background(
                                                color = PklPrimary900,
                                                shape = RoundedCornerShape(
                                                    bottomStart = 15.dp,
                                                    bottomEnd = 15.dp
                                                )
                                            )
                                            .clickable { selectedItemIndex = -1 }
                                            .padding(
                                                top = 10.dp,
                                                bottom = 10.dp
                                            ),
                                        textAlign = TextAlign.Center,
                                        text = stringResource(id = R.string.close_popup_list_ruta).uppercase(),
                                        color = Color.White,
                                        fontFamily = PoppinsFontFamily,
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}


fun openWhatsAppViaIntent(
    phoneNumber: String,
    context: Context
) {
    try {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse("https://wa.me/$phoneNumber")
        context.startActivity(intent)
    } catch (e: Exception) {
        // Handle exception if WhatsApp is not installed or any other issue
        Toast.makeText(context, "WhatsApp tidak terinstall", Toast.LENGTH_SHORT).show()
    }
}

@Composable
fun WilayahKerjaCard(
    listWilayah: List<WilayahEntity>,
    listAllSampelRuta: List<SampelRutaEntity>
) {
    // Second Card - Add another Card element here
    Card(
        modifier = Modifier
            .padding(16.dp)
            .shadow(8.dp)
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = PklPrimary900,
            contentColor = Color.Gray,
        ),
        shape = MaterialTheme.shapes.medium
    ) {
        Column {
            Text(
                text = "WILAYAH KERJA".uppercase(),
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(8.dp),
                fontFamily = PoppinsFontFamily,
                fontWeight = FontWeight.Medium,
                fontSize = 20.sp,
                color = Color.White
            )
            Box(
                modifier = Modifier
                    .background(Color.White)
                    .fillMaxWidth()
                    .padding(12.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Column(
                            modifier = Modifier.fillMaxWidth(0.5f)
                        ) {
                            Text(
                                text = "Kode",
                                fontFamily = PoppinsFontFamily,
                                fontWeight = FontWeight.Medium,
                                modifier = Modifier.align(Alignment.CenterHorizontally)
                            )
                        }
                        Column(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "Titik Sampel",
                                fontFamily = PoppinsFontFamily,
                                fontWeight = FontWeight.Medium,
                                modifier = Modifier.align(Alignment.CenterHorizontally)
                            )
                        }
                    }
                    if (listWilayah.isNotEmpty()) {
                        listWilayah.forEach { wilayah ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ) {
                                Column(
                                    modifier = Modifier.fillMaxWidth(0.5f)
                                ) {
                                    Text(
                                        text = wilayah.noBS,
                                        fontFamily = PoppinsFontFamily,
                                        fontWeight = FontWeight.Medium,
                                        fontSize = 30.sp,
                                        modifier = Modifier.align(Alignment.CenterHorizontally)
                                    )
                                }
                                Column(
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(
                                        text = "${listAllSampelRuta.filter { it.idBS == wilayah.idBS }.size}",
                                        fontFamily = PoppinsFontFamily,
                                        fontWeight = FontWeight.Medium,
                                        fontSize = 30.sp,
                                        modifier = Modifier.align(Alignment.CenterHorizontally)
                                    )
                                }
                            }
                        }
                    } else {
                        // Tampilkan pesan atau lakukan aksi lain jika listWilayah kosong
                        Text(
                            text = "Tidak ada data wilayah.",
                            style = TextStyle(
                                fontFamily = PoppinsFontFamily,
                                fontWeight = FontWeight.Medium,
                                fontSize = 14.sp,
                                platformStyle = PlatformTextStyle(
                                    includeFontPadding = false
                                )
                            )
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun StatusListingCard(
    listWilayah: List<WilayahEntity>
) {
    Card(
        modifier = Modifier
            .padding(16.dp)
            .shadow(8.dp)
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = PklPrimary900,
            contentColor = Color.Gray,
        ),
        shape = MaterialTheme.shapes.medium
    ) {
        Column {
            Text(
                text = "Status Listing".uppercase(),
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(8.dp),
                fontFamily = PoppinsFontFamily,
                fontWeight = FontWeight.Medium,
                fontSize = 20.sp,
                color = Color.White
            )
            Box(
                modifier = Modifier
                    .background(Color.White)
                    .fillMaxWidth()
                    .padding(12.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                start = 10.dp,
                                top = 10.dp,
                                end = 10.dp
                            ),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                listWilayah.forEach { wilayahEntity ->
                                    Column {
                                        Text(
                                            text = wilayahEntity.noBS,
                                            style = TextStyle(
                                                fontFamily = PoppinsFontFamily,
                                                fontWeight = FontWeight.Medium,
                                                fontSize = 18.sp,
                                                platformStyle = PlatformTextStyle(
                                                    includeFontPadding = false
                                                )
                                            ),
                                            color = Color.DarkGray
                                        )
                                        Text(
                                            text = wilayahEntity.namaKel,
                                            style = TextStyle(
                                                fontFamily = PoppinsFontFamily,
                                                fontWeight = FontWeight.Medium,
                                                fontSize = 14.sp,
                                                platformStyle = PlatformTextStyle(
                                                    includeFontPadding = false
                                                )
                                            )
                                        )
                                        Text(
                                            text = wilayahEntity.namaKec,
                                            style = TextStyle(
                                                fontFamily = PoppinsFontFamily,
                                                fontWeight = FontWeight.Medium,
                                                fontSize = 14.sp,
                                                platformStyle = PlatformTextStyle(
                                                    includeFontPadding = false
                                                )
                                            )
                                        )

                                    }
                                    Text(
                                        text = wilayahEntity.status.uppercase(Locale.getDefault()),
                                        style = TextStyle(
                                            fontFamily = PoppinsFontFamily,
                                            fontWeight = FontWeight.Medium,
                                            fontSize = 20.sp,
                                            platformStyle = PlatformTextStyle(
                                                includeFontPadding = false
                                            )
                                        )
                                    )
                                }
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
        }
    }
}

@Composable
fun ProgresCacahCard(
    listWilayah: List<WilayahEntity>,
    listAllSampelRuta: List<SampelRutaEntity>
) {
    Card(
        modifier = Modifier
            .padding(16.dp)
            .shadow(8.dp)
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = PklPrimary900,
            contentColor = Color.Gray,
        ),
        shape = MaterialTheme.shapes.medium
    ) {
        Column {
            Text(
                text = "Progres Cacah".uppercase(),
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(8.dp),
                fontFamily = PoppinsFontFamily,
                fontWeight = FontWeight.Medium,
                fontSize = 20.sp,
                color = Color.White
            )
            Box(
                modifier = Modifier
                    .background(Color.White)
                    .fillMaxWidth()
                    .padding(12.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    listWilayah.forEach { wilayahEntity ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(
                                    start = 10.dp,
                                    top = 10.dp,
                                    end = 10.dp
                                ),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = wilayahEntity.noBS,
                                style = TextStyle(
                                    fontFamily = PoppinsFontFamily,
                                    fontWeight = FontWeight.Medium,
                                    fontSize = 20.sp,
                                    platformStyle = PlatformTextStyle(
                                        includeFontPadding = false
                                    )
                                ),
                                color = Color.DarkGray
                            )
                            AnimatedCircularProgressIndicator(
                                currentValue = listAllSampelRuta.filter { it.status == "1" && it.idBS == wilayahEntity.idBS }.size,
                                maxValue = (if (listAllSampelRuta.filter { it.idBS == wilayahEntity.idBS }.isEmpty()) 10 else listAllSampelRuta.filter { it.idBS == wilayahEntity.idBS }.size),
                                progressBackgroundColor = PklPrimary100,
                                progressIndicatorColor = PklPrimary900,
                                completedColor = PklPrimary900,
                                circularIndicatorDiameter = 66.dp
                            )
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }
            }
        }
    }
}

//@Composable
//fun ProgresCacahCard() {
//
//}

//@Preview
//@Composable
//fun ProgresListingCardPreview() {
//    ProgresListingCard(listWilayah = listOf(wilayah1))
//}
//
//@Preview
//@Composable
//fun StatusListingCardPreview() {
//    val anggotaTim = listOf(mahasiswa1, mahasiswa2)
//
//    StatusListingCard(
//        anggotaTim = anggotaTim
//    )
//}
//
//@Preview
//@Composable
//fun ProfileCardPreview() {
//    ProfileCard(
//        session = sessionPml,
//        dataTim = dataTimPml
//    )
//}
//
//@Preview
//@Composable
//fun PmlCardPreview() {
//    PmlCard(dataTim = dataTimPpl)
//}
//
//@Preview
//@Composable
//fun ListPplCardPreview() {
//    val listMahasiswa = listOf(mahasiswa1, mahasiswa2)
//
//    ListPplCard(anggotaTim = listMahasiswa)
//}
//
//@Preview
//@Composable
//fun WilayahWilayahKerjaCardPreview() {
//    WilayahKerjaCard(listWilayah = listOf(wilayah1))
//}

//val sessionPml = Session(
//    nama = "Falana Rofako",
//    nim = "222112038",
//    avatar = "avatar_falana",
//    isKoor = true,
//    id_kuesioner = "00000",
//    idTim = ""
//)
//
//val sessionPpl = Session(
//    nama = "Satria Baja Hitam",
//    nim = "111222333",
//    avatar = "avatar_satria",
//    isKoor = false,
//    id_kuesioner = "11111",
//    idTim = ""
//)
//
//val wilayah1 = Wilayah(
//    catatan = "Catatan untuk wilayah 1",
//    idKab = "001",
//    idKec = "001",
//    idKel = "001",
//    jmlGenZ = 100,
//    jmlRt = 20,
//    jmlRtGenz = 10,
//    namaKab = "Nama Kabupaten 1",
//    namaKec = "Nama Kecamatan 1",
//    namaKel = "Nama Kelurahan 1",
//    noBS = "444A",
//    ruta = listOf(/*isi dengan instance Ruta*/),
//    status = "ready",
//    tglListing = Date(),
//    tglPeriksa = Date()
//)
//
//val wilayah2 = Wilayah(
//    catatan = "Catatan untuk wilayah 2",
//    idKab = "002",
//    idKec = "002",
//    idKel = "002",
//    jmlGenZ = 200,
//    jmlRt = 40,
//    jmlRtGenz = 20,
//    namaKab = "Nama Kabupaten 2",
//    namaKec = "Nama Kecamatan 2",
//    namaKel = "Nama Kelurahan 2",
//    noBS = "444B",
//    ruta = listOf(/*isi dengan instance Ruta*/),
//    status = "listing",
//    tglListing = Date(),
//    tglPeriksa = Date()
//)
//
//val mahasiswa1 = Mahasiswa(
//    alamat = "Jl. Pemuda No. 123, Surabaya",
//    email = "mahasiswa1@example.com",
//    foto = sessionPpl.avatar,
//    id_tim = "001",
//    isKoor = false,
//    nama = sessionPpl.nama,
//    nim = sessionPpl.nim!!,
//    no_hp = "081234567890",
//    password = "password1",
//    wilayah_kerja = listOf(wilayah1)
//)
//
//val mahasiswa2 = Mahasiswa(
//    alamat = "Jl. Veteran No. 456, Malang",
//    email = "mahasiswa2@example.com",
//    foto = "foto_mahasiswa2.jpg",
//    id_tim = "001",
//    isKoor = false,
//    nama = "Mahasiswa Dua",
//    nim = "87654321",
//    no_hp = "098765432109",
//    password = "password2",
//    wilayah_kerja = listOf(wilayah2)
//)
//
//
//val dataTimPpl = DataTim(
//    anggota = emptyList(),
//    idTim = "001",
//    namaTim = "Tim Modul 1",
//    passPML = "12345678",
//    namaPML = sessionPml.nama,
//    nimPML = sessionPml.nim,
//    teleponPML = "08585858585"
//)
//
//val dataTimPml = DataTim(
//    anggota = listOf(mahasiswa1, mahasiswa2),
//    idTim = "001",
//    namaTim = "Tim Modul 1",
//    passPML = "12345678",
//    namaPML = sessionPml.nama,
//    nimPML = sessionPml.nim,
//    teleponPML = "08585858585"
//)