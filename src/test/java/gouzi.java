/**
 * @Author: liyu.guan
 * @Date: 2019/5/9 上午11:43
 */
public class gouzi {
    public static void main(String[] args) {

        System.out.println("开始");
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Runtime.getRuntime().addShutdownHook( new Thread(){
            public void run(){
                System.out.println("程序关闭了");
            }
        });
    }
}
