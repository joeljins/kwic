 class ConsoleOutput implements Output{

    @Override
    public void write(String content){
        System.out.print(content);
    }
}