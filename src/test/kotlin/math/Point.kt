package math

import org.junit.Assert
import org.junit.Test
import java.lang.IllegalArgumentException

class PointTest {

    @Test
    fun `Should initialize different dimensions`() {
        Assert.assertArrayEquals(
            Point(-1.0, 1.0).coordinates,
            doubleArrayOf(-1.0, 1.0),
            0.0001
        )

        Assert.assertArrayEquals(
            Point(-1.0, 2.0, 3.0, 4.0).coordinates,
            doubleArrayOf(-1.0, 2.0, 3.0, 4.0),
            0.0001
        )
    }

    @Test(expected = IllegalArgumentException::class)
    fun `Should throw exception`() {
        Point(1.0).distance(Point(1.0, 2.0))
    }

    @Test
    fun `Should calculate distance`() {
        Assert.assertEquals(
            2.0,
            Point(1.0).distance(Point(-1.0)),
            0.001
        )


        Assert.assertEquals(
            5.0,
            Point(1.0, 4.0).distance(Point(5.0, 1.0)),
            0.001
        )

        Assert.assertEquals(
            5.196,
            Point(1.0, 1.0, 1.0).distance(Point(4.0, 4.0, 4.0)),
            0.001
        )
    }
}