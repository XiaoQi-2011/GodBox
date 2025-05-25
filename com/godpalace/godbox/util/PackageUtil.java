public static List<String> getClassName(String packageName) throws Exception {
    List<String> classNames = new ArrayList<>();
    ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

    // 使用反射获取类路径
    Method method = ClassLoader.class.getDeclaredMethod("getURLs");
    method.setAccessible(true);
    URL[] urls = (URL[]) method.invoke(classLoader);

    for (URL url : urls) {
        File directory = new File(url.getFile());
        if (directory.exists()) {
            String absolutePath = directory.getAbsolutePath();
            if (absolutePath.contains("!")) {
                // 处理 JAR 包中的类
                String jarPath = absolutePath.substring(0, absolutePath.indexOf("!"));
                classNames.addAll(getClassNameFromJar(jarPath, packageName));
            } else {
                // 处理文件系统中的类
                classNames.addAll(getClassNameFromFile(directory, packageName));
            }
        }
    }
    return classNames;
}

/**
 * 从项目文件获取某包下所有类
 *
 * @param filePath 文件路径
 * @param className 类名集合
 */