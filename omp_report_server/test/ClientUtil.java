import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

/**
 * @auther wendy
 * @since 2016/3/5 15:51
 */
public class ClientUtil {

    public static void sendReport(String url,String content)  throws IOException {
        URL u = new URL(url);
        URLConnection rulConnection = u.openConnection();
        HttpURLConnection httpUrlConnection = (HttpURLConnection) rulConnection;
        // 设置是否向httpUrlConnection输出，因为这个是post请求，参数要放在
        // http正文内，因此需要设为true, 默认情况下是false;
        httpUrlConnection.setDoOutput(true);

        // 设置是否从httpUrlConnection读入，默认情况下是true;
        httpUrlConnection.setDoInput(true);

        // Post 请求不能使用缓存
        httpUrlConnection.setUseCaches(false);

        // 设定传送的内容类型是可序列化的java对象
        // (如果不设此项,在传送序列化对象时,当WEB服务默认的不是这种类型时可能抛java.io.EOFException)
        httpUrlConnection.setRequestProperty("Content-type", "application/json");
        // 设定请求的方法为"POST"，默认是GET
        httpUrlConnection.setRequestMethod("POST");

        // 连接，从上述第2条中url.openConnection()至此的配置必须要在connect之前完成，
        //   httpUrlConnection.connect();

        // 此处getOutputStream会隐含的进行connect(即：如同调用上面的connect()方法，
        // 所以在开发中不调用上述的connect()也可以)。
        OutputStream outStrm = httpUrlConnection.getOutputStream();

        outStrm.write(Bytes.toBytes(content));

        // 现在通过输出流对象构建对象输出流对象，以实现输出可序列化的对象。
        //ObjectOutputStream objOutputStrm = new ObjectOutputStream(outStrm);

       // objOutputStrm.writeObject(content);

        // 刷新对象输出流，将任何字节都写入潜在的流中（些处为ObjectOutputStream）
       // objOutputStrm.flush();

        // 关闭流对象。此时，不能再向对象输出流写入任何数据，先前写入的数据存在于内存缓冲区中,
        // 在调用下边的getInputStream()函数时才把准备好的http请求正式发送到服务器
        //objOutputStrm.close();

        // 调用HttpURLConnection连接对象的getInputStream()函数,
        // 将内存缓冲区中封装好的完整的HTTP请求电文发送到服务端。
        httpUrlConnection.getInputStream(); // <===注意，实际发送请求的代码段就在这里

    }

}
