/**
 * 
 */
package datascript.test;

import java.io.File;
import java.io.IOException;

import javax.imageio.stream.FileImageOutputStream;

import junit.framework.TestCase;
import bits.IntegerArray;
import datascript.runtime.ByteArray;
import datascript.runtime.IntArray;
import datascript.runtime.ShortArray;
import datascript.runtime.UnsignedByteArray;
import datascript.runtime.UnsignedIntArray;
import datascript.runtime.UnsignedShortArray;

/**
 * @author HWellmann
 *
 */
public class UnsignedArrayTest extends TestCase
{
    private FileImageOutputStream os;
    private String fileName = "unsignedarraytest.bin";
    private File file = new File(fileName);

    /**
     * Constructor for BitStreamReaderTest.
     * @param name
     */
    public UnsignedArrayTest(String name)
    {
        super(name);
    }

    /*
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception
    {
    }

    /*
     * @see TestCase#tearDown()
     */
    protected void tearDown() throws Exception
    {
        file.delete();
    }

    /*
     * Test method for 'datascript.library.BitStreamReader.readByte()'
     */
    private void writeArray(int numElems, 
            int startInt8, int startUInt8,
            int startInt16, int startUInt16,
            int startInt32, int startUInt32) throws IOException
    {
        file.delete();
        os = new FileImageOutputStream(file);
        os.writeShort(numElems);

        for (int i = startInt8; i < startInt8+numElems; i++)
        {
            os.writeByte(i);
        }
        for (int i = startUInt8; i < startUInt8+numElems; i++)
        {
            os.writeByte(i);
        }
        for (int i = startInt16; i < startInt16+numElems; i++)
        {
            os.writeShort(i);
        }
        for (int i = startUInt16; i < startUInt16+numElems; i++)
        {
            os.writeShort(i);
        }
        for (int i = startInt32; i < startInt32+numElems; i++)
        {
            os.writeInt(i);
        }
        for (int i = startUInt32; i < startUInt32+numElems; i++)
        {
            os.writeInt(i);
        }
        os.close();

        IntegerArray array = new IntegerArray(fileName);
        assertEquals(numElems, array.getNumElems());

        ByteArray signed = array.getInt8List();
        for (int i = 0; i < numElems; i++)
        {            
            assertEquals(startInt8+i, signed.elementAt(i));
        }
        UnsignedByteArray unsigned = array.getUint8List();
        for (int i = 0; i < numElems; i++)
        {            
            assertEquals(startUInt8+i, unsigned.elementAt(i));
        }

        ShortArray signed16 = array.getInt16List();
        for (int i = 0; i < numElems; i++)
        {            
            assertEquals(startInt16+i, signed16.elementAt(i));
        }
        UnsignedShortArray unsigned16 = array.getUint16List();
        for (int i = 0; i < numElems; i++)
        {            
            assertEquals(startUInt16+i, unsigned16.elementAt(i));
        }

        IntArray signed32 = array.getInt32List();
        for (int i = 0; i < numElems; i++)
        {            
            assertEquals(startInt32+i, signed32.elementAt(i));
        }
        UnsignedIntArray unsigned32 = array.getUint32List();
        for (int i = 0; i < numElems; i++)
        {            
            assertEquals(startUInt32+i, unsigned32.elementAt(i));
        }
    }

    public void testArray1() throws IOException
    {
        writeArray(5, 2, 3, 2000, 3000, 200000, 300000);
    }

    public void testArray2() throws IOException
    {
        writeArray(5, -120, 120, -120, 120, -120, 120);
    }

    public void testArray3() throws IOException
    {
        writeArray(5, -120, 120, -32000, 32000, -32000, 32000);
    }

    public void testArray4() throws IOException
    {
        writeArray(5, -120, 120, -32000, 32000, 0xF0000000, 0x88008800);
    }

}
