import io.netty.util.internal.SystemPropertyUtil;

/**
 * @Author: liyu.guan
 * @Date: 2019/5/7 下午12:11
 */
public class TestMain {

    public static void main(String[] args) {
        System.out.println(Runtime.getRuntime().availableProcessors());
        System.out.println(Math.max(1, SystemPropertyUtil.getInt("io.netty.eventLoopThreads", Runtime.getRuntime().availableProcessors() * 2)));
    }
}
