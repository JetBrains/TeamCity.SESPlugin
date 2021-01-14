/*
 * Copyright 2000-2021 JetBrains s.r.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package jetbrains.buildServer.sesPlugin.teamcity.util

import com.google.gson.Gson
import jetbrains.buildServer.sesPlugin.data.GsonContainer
import org.springframework.web.servlet.view.AbstractView
import java.io.IOException
import java.io.OutputStream
import java.io.OutputStreamWriter
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class GsonView : AbstractView() {
    private val gson = Gson()

    private val encoding = "UTF-8"

    init {
        contentType = "application/json"
        isExposePathVariables = false
    }

    override fun renderMergedOutputModel(model: MutableMap<String, Any>, request: HttpServletRequest, response: HttpServletResponse) {
        val stream = response.outputStream

        val value = this.filterAndWrapModel(model, request)

        this.writeContent(stream as OutputStream, value)
    }


    @Throws(IOException::class)
    private fun writeContent(stream: OutputStream, data: GsonContainer) {
        val jsonWriter = gson.newJsonWriter(OutputStreamWriter(stream, encoding))

        gson.toJson(gson.toJsonTree(data.model), jsonWriter)

        jsonWriter.flush()
    }

    private fun filterAndWrapModel(model: Map<String, Any>, request: HttpServletRequest): GsonContainer {
        val value = filterModel(model)

        val resultModel = if (value.keys.size == 1) {
            value.values.first()
        } else {
            value
        }
        return GsonContainer(resultModel)
    }

    /**
     * It's supposed that custom serializers and filters are stored in the model among with data
     */
    private fun filterModel(model: Map<String, Any>): Map<String, Any> {
        return model.filterValues { it is JsonModelComponent }
    }
}