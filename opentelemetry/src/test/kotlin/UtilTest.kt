import io.opentelemetry.api.trace.TraceId
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import java.util.UUID
import kotlin.test.assertFalse


class UtilTest {


    @Test
    fun testValidTraceId() {
        repeat(10) {
            assertTrue { TraceId.isValid(UUID.randomUUID().toString().replace("-", "")) }
        }
    }

    @ParameterizedTest
    @CsvSource(
        value = [
            "1",
            "u",
            "abcdefghijklmnopqrstuvwxyz"
        ]
    )
    fun testInvalidTraceId(id: String) {
        assertFalse { TraceId.isValid(id) }
    }

}