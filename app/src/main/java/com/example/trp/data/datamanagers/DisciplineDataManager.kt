package com.example.trp.data.datamanagers

import android.annotation.SuppressLint
import android.content.Context
import androidx.datastore.core.Serializer
import androidx.datastore.dataStore
import com.example.trp.data.disciplines.Disciplines
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream

@SuppressLint("StaticFieldLeak")
object DisciplinesDataManager {
    private val Context.disciplinesDataStore by dataStore("Disciplines.json", DisciplinesSerializer)

    private lateinit var context: Context

    fun initialize(context: Context) {
        DisciplinesDataManager.context = context
    }

    object DisciplinesSerializer : Serializer<Disciplines> {
        override val defaultValue: Disciplines
            get() = Disciplines()

        override suspend fun readFrom(input: InputStream): Disciplines {
            return try {
                Json.decodeFromString(
                    deserializer = Disciplines.serializer(),
                    string = input.readBytes().decodeToString()
                )
            } catch (e: SerializationException) {
                e.printStackTrace()
                Disciplines()
            }
        }

        override suspend fun writeTo(t: Disciplines, output: OutputStream) {
            withContext(Dispatchers.IO) {
                output.write(
                    Json.encodeToString(
                        serializer = Disciplines.serializer(),
                        value = t
                    ).encodeToByteArray()
                )
            }
        }
    }

    fun getDisciplines() = context.disciplinesDataStore.data

    suspend fun saveDisciplines(disciplines: Disciplines) {
        context.disciplinesDataStore.updateData { disciplines }
    }
}