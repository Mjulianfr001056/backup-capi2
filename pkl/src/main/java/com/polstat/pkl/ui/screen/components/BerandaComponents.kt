package com.polstat.pkl.ui.screen.components

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.polstat.pkl.model.domain.DataTim
import com.polstat.pkl.model.domain.Mahasiswa
import com.polstat.pkl.model.domain.User
import com.polstat.pkl.model.domain.Wilayah
import com.polstat.pkl.ui.theme.PklPrimary100
import com.polstat.pkl.ui.theme.PklPrimary900
import com.polstat.pkl.ui.theme.PoppinsFontFamily
import java.util.Date
import java.util.Locale

@Composable
fun ProfileCard(
    user: User,
    dataTim: DataTim
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
                text = user.nama,
                modifier = Modifier.align(Alignment.CenterHorizontally),
                fontFamily = PoppinsFontFamily,
                fontWeight = FontWeight.Medium
            )
            Row(
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text(
                    text = user.nim,
                    fontFamily = PoppinsFontFamily,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = "   |   ",
                    fontFamily = PoppinsFontFamily,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = if (user.isKoor) "PCL" else "PPL",
                    fontFamily = PoppinsFontFamily,
                    fontWeight = FontWeight.Medium
                )
            }
            Row(
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text(
                    text = dataTim.namaTim,
                    fontFamily = PoppinsFontFamily,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = " - ",
                    fontFamily = PoppinsFontFamily,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = dataTim.idTim,
                    fontFamily = PoppinsFontFamily,
                    fontWeight = FontWeight.Medium
                )
                Log.d("Beranda", dataTim.anggota.size.toString())
            }
        }
    }
}

@Composable
fun PmlCard(
    dataTim: DataTim
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
                        text = dataTim.namaPML,
                        fontFamily = PoppinsFontFamily,
                        fontWeight = FontWeight.Medium,
                        fontSize = 18.sp,
                        color = Color.DarkGray
                    )
                    Text(
                        text = dataTim.nimPML,
                        fontFamily = PoppinsFontFamily,
                        fontWeight = FontWeight.Medium,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}

@Composable
fun ListPplCard(
    listMahasiswa: List<Mahasiswa>
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
                    if (listMahasiswa.isNotEmpty()) {
                        listMahasiswa.forEach { mahasiswa ->
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
                                    Text(
                                        text = mahasiswa.nama,
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
                                        text = mahasiswa.nim,
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
                                Column {
                                    println(mahasiswa.nama)
                                    println(mahasiswa.wilayah_kerja.size)
                                    if (mahasiswa.wilayah_kerja.isNotEmpty()) {
                                        mahasiswa.wilayah_kerja.forEach { wilayah ->
                                            Row(
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Text(
                                                    text = wilayah.noBS,
                                                    style = TextStyle(
                                                        fontFamily = PoppinsFontFamily,
                                                        fontWeight = FontWeight.Medium,
                                                        fontSize = 18.sp,
                                                        platformStyle = PlatformTextStyle(
                                                            includeFontPadding = false
                                                        )
                                                    )
                                                )
                                                Spacer(modifier = Modifier.width(16.dp))
                                                AnimatedCircularProgressIndicator(
                                                    currentValue = wilayah.ruta.size,
                                                    maxValue = if (wilayah.jmlRt == 0) 99 else wilayah.jmlRt,
                                                    progressBackgroundColor = PklPrimary100,
                                                    progressIndicatorColor = PklPrimary900,
                                                    completedColor = PklPrimary900,
                                                    circularIndicatorDiameter = 66.dp
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.height(12.dp))
                        }
                    } else {
                        // Tampilkan pesan atau lakukan aksi lain jika listMahasiswa kosong
                        Text(
                            text = "Tidak ada data mahasiswa.",
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
fun WilayahKerjaCard(
    listWilayah: List<Wilayah>
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
                                        text = "0",
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
    anggotaTim: List<Mahasiswa>
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
                    if (anggotaTim.isNotEmpty()) {
                        anggotaTim.forEach { mahasiswa ->
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
                                    if (mahasiswa.wilayah_kerja.isNotEmpty()) {
                                        mahasiswa.wilayah_kerja.forEach { wilayah ->
                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.SpaceBetween,
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Column {
                                                    Text(
                                                        text = wilayah.noBS,
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
                                                        text = wilayah.namaKel,
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
                                                        text = wilayah.namaKec,
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
                                                    text = wilayah.status.uppercase(Locale.getDefault()),
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
                            }
                            Spacer(modifier = Modifier.height(12.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ProgresListingCard(
    listWilayah: List<Wilayah>
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
                text = "Progres Listing".uppercase(),
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
                    if (listWilayah.isNotEmpty()) {
                        listWilayah.forEach { wilayah ->
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
                                    Text(
                                        text = wilayah.noBS,
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
                                }
                                AnimatedCircularProgressIndicator(
                                    currentValue = wilayah.ruta.size,
                                    maxValue = if (wilayah.jmlRt == 0) 99 else wilayah.jmlRt,
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
}

//@Composable
//fun ProgresCacahCard() {
//
//}

@Preview
@Composable
fun ProgresListingCardPreview() {
    ProgresListingCard(listWilayah = listOf(wilayah1))
}

@Preview
@Composable
fun StatusListingCardPreview() {
    val anggotaTim = listOf(mahasiswa1, mahasiswa2)

    StatusListingCard(
        anggotaTim = anggotaTim
    )
}

@Preview
@Composable
fun ProfileCardPreview() {
    ProfileCard(
        user = userPml,
        dataTim = dataTimPml
    )
}

@Preview
@Composable
fun PmlCardPreview() {
    PmlCard(dataTim = dataTimPpl)
}

@Preview
@Composable
fun ListPplCardPreview() {
    val listMahasiswa = listOf(mahasiswa1, mahasiswa2)

    ListPplCard(listMahasiswa = listMahasiswa)
}

@Preview
@Composable
fun WilayahWilayahKerjaCardPreview() {
    WilayahKerjaCard(listWilayah = listOf(wilayah1))
}

val userPml = User(
    nama = "Falana Rofako",
    nim = "222112038",
    avatar = "avatar_falana",
    isKoor = true,
    id_kuesioner = "00000"
)

val userPpl = User(
    nama = "Satria Baja Hitam",
    nim = "111222333",
    avatar = "avatar_satria",
    isKoor = false,
    id_kuesioner = "11111"
)

val wilayah1 = Wilayah(
    catatan = "Catatan untuk wilayah 1",
    idKab = "001",
    idKec = "001",
    idKel = "001",
    jmlGenZ = 100,
    jmlRt = 20,
    jmlRtGenz = 10,
    namaKab = "Nama Kabupaten 1",
    namaKec = "Nama Kecamatan 1",
    namaKel = "Nama Kelurahan 1",
    noBS = "444A",
    ruta = listOf(/*isi dengan instance Ruta*/),
    status = "ready",
    tglListing = Date(),
    tglPeriksa = Date()
)

val wilayah2 = Wilayah(
    catatan = "Catatan untuk wilayah 2",
    idKab = "002",
    idKec = "002",
    idKel = "002",
    jmlGenZ = 200,
    jmlRt = 40,
    jmlRtGenz = 20,
    namaKab = "Nama Kabupaten 2",
    namaKec = "Nama Kecamatan 2",
    namaKel = "Nama Kelurahan 2",
    noBS = "444B",
    ruta = listOf(/*isi dengan instance Ruta*/),
    status = "listing",
    tglListing = Date(),
    tglPeriksa = Date()
)

val mahasiswa1 = Mahasiswa(
    alamat = "Jl. Pemuda No. 123, Surabaya",
    email = "mahasiswa1@example.com",
    foto = userPpl.avatar,
    id_tim = "001",
    isKoor = false,
    nama = userPpl.nama,
    nim = userPpl.nim,
    no_hp = "081234567890",
    password = "password1",
    wilayah_kerja = listOf(wilayah1)
)

val mahasiswa2 = Mahasiswa(
    alamat = "Jl. Veteran No. 456, Malang",
    email = "mahasiswa2@example.com",
    foto = "foto_mahasiswa2.jpg",
    id_tim = "001",
    isKoor = false,
    nama = "Mahasiswa Dua",
    nim = "87654321",
    no_hp = "098765432109",
    password = "password2",
    wilayah_kerja = listOf(wilayah2)
)


val dataTimPpl = DataTim(
    anggota = emptyList(),
    idTim = "001",
    namaTim = "Tim Modul 1",
    passPML = "12345678",
    namaPML = userPml.nama,
    nimPML = userPml.nim,
    teleponPML = "08585858585"
)

val dataTimPml = DataTim(
    anggota = listOf(mahasiswa1, mahasiswa2),
    idTim = "001",
    namaTim = "Tim Modul 1",
    passPML = "12345678",
    namaPML = userPml.nama,
    nimPML = userPml.nim,
    teleponPML = "08585858585"
)