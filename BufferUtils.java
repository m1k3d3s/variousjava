
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

public class BufferUtils
{
    public static final int SIZEOF_BYTE = Byte.SIZE/Byte.SIZE;
    public static final int SIZEOF_CHAR = Character.SIZE/Byte.SIZE;
    public static final int SIZEOF_SHORT = Short.SIZE/Byte.SIZE;
    public static final int SIZEOF_INT = Integer.SIZE/Byte.SIZE;
    public static final int SIZEOF_LONG = Long.SIZE/Byte.SIZE;
    public static final int SIZEOF_FLOAT = Float.SIZE/Byte.SIZE;
    public static final int SIZEOF_DOUBLE = Double.SIZE/Byte.SIZE;

    /**
     * put ascii string into a bytebuffer
     */
    public static void put(ByteBuffer b, CharSequence s)
    {
        put(b,s,0,s.length());
    }

    /**
     * put ascii string into a bytebuffer
     */
    public static void put(ByteBuffer b, CharSequence s, int start, int len)
    {
        len = Math.min(len,b.remaining());
        int end = Math.min(start+len,s.length());
        for (; start < end; ++start)
        {
            b.put((byte)s.charAt(start));
        }
    }

    /**
     * put short as ascii string into a byte buffer
     */
    public static void put(ByteBuffer buffer, short value)
    {
        put(buffer,value,NumberUtils.digits(value),(byte)0);
    }

    public static void put(ByteBuffer buffer, short value, int len)
    {
        put(buffer,value,len,(byte)0);
    }

    public static void put(ByteBuffer buffer, short value, int len, byte pad)
    {
        int start = buffer.position();
        int offset = start + len - 1;

        if (value == 0)
        {
            buffer.put(offset--,Base64.ZERO);
        }
        else if (value > 0)
        {
            while ((offset >= start) && (value > 0))
            {
                short num = (short)(value / 10);
                short rem = (short)(value % 10);
                byte b = (byte)(Base64.ZERO + (rem));
                buffer.put(offset--,b);
                value = num;
            }
        }
        else
        {
            while ((offset >= start) && (value < 0))
            {
                short num = (short)(value / 10);
                short rem = (short)(0 - (value % 10));
                byte b = (rem == 0) ? Base64.ZERO : (byte)(Base64.ZERO + (rem));
                buffer.put(offset--,b);
                value = num;
            }

            if (offset >= start)
            {
                buffer.put(offset--,(byte)'-');
            }
        }

        if (pad != 0)
        {
            while (offset >= start)
            {
                buffer.put(offset--,pad);
            }
        }

        buffer.position(start+len);
    }


    /**
     * put int as ascii string into a byte buffer
     */
    public static void put(ByteBuffer buffer, int value)
    {
        put(buffer,value,NumberUtils.digits(value),(byte)0);
    }

    public static void put(ByteBuffer buffer, int value, int len)
    {
        put(buffer,value,len,(byte)0);
    }

    public static void put(ByteBuffer buffer, int value, int len, byte pad)
    {
        int start = buffer.position();
        int offset = start + len - 1;

        if (value == 0)
        {
            buffer.put(offset--,Base64.ZERO);
        }
        else if (value > 0)
        {
            while ((offset >= start) && (value > 0))
            {
                int num = value / 10;
                int rem = value % 10;
                byte b = (byte)(Base64.ZERO + (rem));
                buffer.put(offset--,b);
                value = num;
            }
        }
        else
        {
            while ((offset >= start) && (value < 0))
            {
                int num = value / 10;
                int rem = 0 - (value % 10);
                byte b = (rem == 0) ? Base64.ZERO : (byte)(Base64.ZERO + (rem));
                buffer.put(offset--,b);
                value = num;
            }

            if (offset >= start)
            {
                buffer.put(offset--,(byte)'-');
            }
        }

        if (pad != 0)
        {
            while (offset >= start)
            {
                buffer.put(offset--,pad);
            }
        }

        buffer.position(start+len);
    }

    /**
     * put long as ascii string into a byte buffer
     */
    public static void put(ByteBuffer buffer, long value)
    {
        put(buffer,value,NumberUtils.digits(value),(byte)0);
    }

    public static void put(ByteBuffer buffer, long value, int len)
    {
        put(buffer,value,len,(byte)0);
    }

    public static void put(ByteBuffer buffer, long value, int len, byte pad)
    {
        int start = buffer.position();
        int offset = start + len - 1;

        if (value == 0)
        {
            buffer.put(offset--,Base64.ZERO);
        }
        else if (value > 0)
        {
            while ((offset >= start) && (value > 0))
            {
                long num = value / 10;
                long rem = value % 10;
                byte b = (byte)(Base64.ZERO + (rem));
                buffer.put(offset--,b);
                value = num;
            }
        }
        else
        {
            while ((offset >= start) && (value < 0))
            {
                long num = value / 10;
                long rem = 0 - (value % 10);
                byte b = (rem == 0) ? Base64.ZERO : (byte)(Base64.ZERO + (rem));
                buffer.put(offset--,b);
                value = num;
            }

            if (offset >= start)
            {
                buffer.put(offset--,(byte)'-');
            }
        }

        if (pad != 0)
        {
            while (offset >= start)
            {
                buffer.put(offset--,pad);
            }
        }

        buffer.position(start+len);
    }

    /**
     * put int as ascii string into a byte buffer
     */
    public static void put(byte[] buffer, int offset, int value, int len)
    {
        int end = offset + len - 1;

        if (value == 0)
        {
            buffer[end--] = Base64.ZERO;
        }
        else
        {
            while ((end >= offset) && (value > 0))
            {
                int num = value / 10;
                byte b = (byte)(Base64.ZERO + (value - (num*10)));
                buffer[end--] = b;
                value = num;
            }
        }
    }

    /**
     * put long as ascii string into a byte buffer
     */
    public static void put(byte[] buffer, int offset, long value, int len)
    {
        int end = offset + len - 1;

        if (value == 0)
        {
            buffer[end--] = Base64.ZERO;
        }
        else
        {
            while ((end >= offset) && (value > 0))
            {
                long num = value / 10;
                byte b = (byte)(Base64.ZERO + (value - (num*10)));
                buffer[end--] = b;
                value = num;
            }
        }
    }

    public static byte[] toArray(ByteBuffer buf)
    {
        byte[] output = new byte[buf.remaining()];

        int pos = buf.position();
        buf.get(output);
        buf.position(pos);

        return output;
    }

    public static String toString(ByteBuffer buf)
    {
        StringBuilder output = new StringBuilder();
        while (buf.hasRemaining()) { output.append((char)buf.get()); }
        return output.toString();
    }

    public static String toString(ByteBuffer buf, int pos, int len)
    {
        StringBuilder output = new StringBuilder();
        int end = pos+len;
        for (int i = pos; i < end; ++i) { output.append((char)buf.get(i)); }
        return output.toString();
    }

    public static StringBuilder toString(ByteBuffer buf, StringBuilder output)
    {
        while (buf.hasRemaining()) { output.append((char)buf.get()); }
        return output;
    }

    public static StringBuilder toString(ByteBuffer buf, StringBuilder output, int pos, int len)
    {
        int end = pos+len;
        for (int i = pos; i < end; ++i) { output.append((char)buf.get(i)); }
        return output;
    }

    public static void copy(ByteBuffer src, int srcPos, ByteBuffer dest, int destPos, int length)
    {
        if (src.limit() < (srcPos + length) || dest.limit() < (destPos + length))
        {
            throw new IndexOutOfBoundsException("Source or destination buffer limites are too small to copy from/to specified position and length.");
        }

        int srcPosition = dest.position();
        int destPosition = src.position();

        if (src != dest)
        {
            int srcLimit = src.limit();

            dest.position(destPos);
            src.position(srcPos);
            src.limit(srcPos + length);
            
            dest.put(src);

            dest.position(destPosition);
            src.position(srcPosition);
            src.limit(srcLimit);
            return;
        }

        if (srcPos < destPos)
        {
            int i = srcPos + length - 1;
            int j = destPos + length - 1;
            int end = srcPos;

            for ( ; i >= end; i--, j--)
            {
                src.position(i);
                byte b = src.get();

                dest.position(j);
                dest.put(b);
            }
         }
        else if (srcPos > destPos)
        {
            int i = srcPos;
            int j = destPos;
            int end = srcPos + length;

            for ( ; i < end; i++, j++)
            {
                src.position(i);
                byte b = src.get();
                
                dest.position(j);
                dest.put(b);
            }
        }

        dest.position(destPosition);
        src.position(srcPosition);
    }

    public static ByteBuffer getCopy(ByteBuffer[] bufs)
    {
        return getCopy(bufs,BufferPool.instance());
    }

    public static ByteBuffer getCopy(ByteBuffer[] bufs, BufferPool pool)
    {
        int len = 0;
        for (ByteBuffer buf : bufs)
        {
            len += buf.remaining();
        }

        ByteBuffer output = pool.getSufficient(len);
        for (ByteBuffer buf : bufs)
        {
            int pos = buf.position();
            output.put(buf);
            buf.position(pos);
        }
        output.flip();
        return output;
    }

    public static ByteBuffer getCopy(ByteBuffer buf)
    {
        return getCopy(buf,BufferPool.instance());
    }

    public static ByteBuffer getCopy(ByteBuffer buf, BufferPool pool)
    {
        ByteBuffer output = pool.getSufficient(buf.remaining());
        output.put(buf);
        output.flip();
        return output;
    }

    public static int bcdToInt(ByteBuffer buffer, int size)
    {
        int value = 0;
        for (int i = 0; i < size; ++i)
        {
            byte b = buffer.get();
            value = (value*100) + ((b >> 4 & 0xF)*10) + (b & 0xf0 >> 4);
        }
        return value;
    }

    public static long bcdToLong(ByteBuffer buffer, int size)
    {
        long value = 0;
        for (int i = 0; i < size; ++i)
        {
            byte b = buffer.get();
            value = (value*100) + ((b >> 4 & 0xF)*10) + (b & 0xf0 >> 4);
        }
        return value;
    }

    public static short buildShort(byte b1, byte b2)
    {
        return (short)(((b1 & 0xff) << 8) |
                       ((b2 & 0xff) << 0));
    }

    public static short getShort(byte[] buf, int off)
    {
        return buildShort(buf[off],buf[off+1]);
    }

    public static void putShort(short val, byte[] buf, int off)
    {
        buf[off] = (byte)(val >> 8);
        buf[off+1] = (byte)(val);
    }

    public static int buildInt(byte b1, byte b2, byte b3, byte b4)
    {
        return (int)(((b1 & 0xff) << 24) |
                     ((b2 & 0xff) << 16) |
                     ((b3 & 0xff) << 8) |
                     ((b4 & 0xff) << 0));
    }

    public static int getInt(byte[] buf, int off)
    {
        return buildInt(buf[off],buf[off+1],buf[off+2],buf[off+3]);
    }

    public static void putInt(int val, byte[] buf, int off)
    {
        buf[off] = (byte)(val >> 24);
        buf[off+1] = (byte)(val >> 16);
        buf[off+2] = (byte)(val >> 8);
        buf[off+3] = (byte)(val);
    }

    public static long buildLong(byte b1, byte b2, byte b3, byte b4, byte b5, byte b6, byte b7, byte b8)
    {
        return (long)(((b1 & 0xffl) << 56) |
                      ((b2 & 0xffl) << 48) |
                      ((b3 & 0xffl) << 40) |
                      ((b4 & 0xffl) << 32) |
                      ((b5 & 0xffl) << 24) |
                      ((b6 & 0xffl) << 16) |
                      ((b7 & 0xffl) << 8) |
                      ((b8 & 0xffl) << 0));
    }

    public static long getLong(byte[] buf, int off)
    {
        return buildLong(buf[off],buf[off+1],buf[off+2],buf[off+3],buf[off+4],buf[off+5],buf[off+6],buf[off+7]);
    }

    public static void putLong(long val, byte[] buf, int off)
    {
        buf[off] = (byte)(val >> 56);
        buf[off+1] = (byte)(val >> 48);
        buf[off+2] = (byte)(val >> 40);
        buf[off+3] = (byte)(val >> 32);
        buf[off+4] = (byte)(val >> 24);
        buf[off+5] = (byte)(val >> 16);
        buf[off+6] = (byte)(val >> 8);
        buf[off+7] = (byte)(val);
    }

    public static boolean equals(ByteBuffer buf1, int pos1, int len1, byte[] buf2, int pos2, int len2)
    {
        if (len1 != len2) { return false; }

        for (int i = 0; i < len1; ++i)
        {
            if (buf1.get(pos1+i) != buf2[pos2+i]) { return false; }
        }
        return true;
    }

    public static boolean equals(ByteBuffer buf1, int pos1, int len1, ByteBuffer buf2, int pos2, int len2)
    {
        if (len1 != len2) { return false; }

        int p1 = buf1.position();
        int l1 = buf1.limit();

        int p2 = buf2.position();
        int l2 = buf2.limit();

        buf1.limit(pos1+len1).position(pos1);
        buf2.limit(pos2+len2).position(pos2);

        boolean output = buf1.equals(buf2);

        buf1.limit(l1).position(p1);
        buf2.limit(l2).position(p2);

        return output;
    }

    public static int compareTo(byte[] buf1, int pos1, int len1, ByteBuffer buf2, int pos2, int len2)
    {
        if (len1 != len2) { return len1 < len2 ? -1 : 1; }

        for (int i = 0; i < len1; ++i)
        {
            byte v1 = buf1[pos1+i];
            byte v2 = buf2.get(pos2+i);
            
            if (v1 == v2) { continue; }
            return v1 < v2 ? -1 : 1;
        }

        return 0;
    }

    public static int compareTo(ByteBuffer buf1, int pos1, int len1, byte[] buf2, int pos2, int len2)
    {
        if (len1 != len2) { return len1 < len2 ? -1 : 1; }

        for (int i = 0; i < len1; ++i)
        {
            byte v1 = buf1.get(pos1+i);
            byte v2 = buf2[pos2+i];
            
            if (v1 == v2) { continue; }
            return v1 < v2 ? -1 : 1;
        }

        return 0;
    }

    public static int compareTo(ByteBuffer buf1, int pos1, int len1, ByteBuffer buf2, int pos2, int len2)
    {
        if (len1 != len2) { return len1 < len2 ? -1 : 1; }

        for (int i = 0; i < len1; ++i)
        {
            byte v1 = buf1.get(pos1+i);
            byte v2 = buf2.get(pos2+i);

            if (v1 == v2) { continue; }
            return v1 < v2 ? -1 : 1;
        }
        
        return 0;
    }

    public static ByteBuffer readFile(File f)
        throws IOException
    {
        return readBuffer(FileUtils.bufferStream(new FileInputStream(f)));
    }

    public static ByteBuffer readBuffer(InputStream is)
        throws IOException
    {
        int available = is.available();
        if (available <= 0) { available = 64 * 1024; }
        ByteBuffer output = BufferPool.instance().getSufficient(available+1);

        byte[] data = new byte[64*1024];
        int read;
        while ((read = is.read(data)) > 0)
        {
            if (output.remaining() < read)
            {
                ByteBuffer tmp = BufferPool.instance().getSufficient(output.capacity() + 64*1024);
                output.flip();
                tmp.put(output);
                BufferPool.free(output);
                output = tmp;
            }

            output.put(data,0,read);
        }
        
        output.flip();
        return output;
    }

    public static void clear(ByteBuffer buffer)
    {
        buffer.clear();
        fill(buffer,(byte)0);
        buffer.clear();
    }

    private static final ByteBuffer fillBuf = BufferPool.instance().get(256);
    public static void fill(ByteBuffer buf, byte b)
    {
        if (b == 0)
        {
            int i = 0;

            while (buf.hasRemaining())
            {
                int r = Math.min(buf.remaining(),fillBuf.capacity());
                fillBuf.limit(r).position(0);
                buf.put(fillBuf);
            }
            
        }
        else
        {
            while (buf.hasRemaining())
            {
                buf.put(b);
            }
        }
    }
}
