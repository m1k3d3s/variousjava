
public class ArchitectureUtils
{
    public enum Architecture
    {
        linux64,
        linux32,
        osx64,
        osx32,
        win64,
        win32,
        java64,
        java32;
    }

    private static class Singleton
    {
        static Architecture architecture;

        static {
            String os = (String)System.getProperty("os.name");
            int vmBits = Integer.parseInt((String)System.getProperty("sun.arch.data.model"));

            StringBuilder sb = new StringBuilder();

            if (os.indexOf("Windows") != -1)
            {
                sb.append("win");
            }
            else if (os.indexOf("Linux") != -1)
            {
                sb.append("linux");
            }
            else if (os.equals("Mac OS X"))
            {
                sb.append("osx");
            }
            else
            {
                sb.append("java");
            }

            sb.append(vmBits);

            try {
                architecture = Architecture.valueOf(sb.toString());
            }
            catch (Exception ex)
            {
                switch (vmBits)
                {
                case 64:
                    architecture = Architecture.java64;
                    break;
                case 32:
                default:
                    architecture = Architecture.java32;
                    break;
                }
            }
        }
    }

    public static Architecture getArchitecture()
    {
        return Singleton.architecture;
    }
}
