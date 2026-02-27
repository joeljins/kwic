 class ConsoleOutput implements Output{
    public ConsoleOutput() {
    }

    @Override
    public void write(String content){
        System.out.print(content);
    }

    @Override
    public void setFilePath(String path) {}
}