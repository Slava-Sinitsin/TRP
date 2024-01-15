package com.example.trp.data.datamanagers

import android.annotation.SuppressLint
import android.content.Context
import androidx.datastore.core.Serializer
import androidx.datastore.dataStore
import com.example.trp.data.mappers.disciplines.Disciplines
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
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

    object DisciplinesSerializer : Serializer<com.example.trp.data.mappers.disciplines.Disciplines> {
        override val defaultValue: com.example.trp.data.mappers.disciplines.Disciplines
            get() = com.example.trp.data.mappers.disciplines.Disciplines()

        override suspend fun readFrom(input: InputStream): com.example.trp.data.mappers.disciplines.Disciplines {
            return try {
                Json.decodeFromString(
                    deserializer = com.example.trp.data.mappers.disciplines.Disciplines.serializer(),
                    string = input.readBytes().decodeToString()
                )
            } catch (e: SerializationException) {
                e.printStackTrace()
                com.example.trp.data.mappers.disciplines.Disciplines()
            }
        }

        override suspend fun writeTo(t: com.example.trp.data.mappers.disciplines.Disciplines, output: OutputStream) {
            withContext(Dispatchers.IO) {
                output.write(
                    Json.encodeToString(
                        serializer = com.example.trp.data.mappers.disciplines.Disciplines.serializer(),
                        value = t
                    ).encodeToByteArray()
                )
            }
        }
    }

    suspend fun getDisciplines() = context.disciplinesDataStore.data.first().list

    suspend fun saveDisciplines(disciplines: com.example.trp.data.mappers.disciplines.Disciplines) {
        context.disciplinesDataStore.updateData { disciplines }
    }
}