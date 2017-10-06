package me.mymilkbottles.weixinqing.log.kafka.producer;

import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import ch.qos.logback.core.Appender;
import ch.qos.logback.core.spi.AppenderAttachableImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


public class KafkaAppender<E> extends KafkaAppenderConfig<E>{


	private static ExecutorService exec = Executors.newFixedThreadPool(5);
	private final AppenderAttachableImpl<E> aai = new AppenderAttachableImpl<E>();
	//完整的一条logs数据  es的field名称
	public static String MSG_FULL_DATA_KEY = "DATA"; 
	public KafkaAppender(){
	}

	@Override
	public void start() {
		//加载初始化参数
		if( !checkPrerequisites() ){
			addError("kafka appender 初始化参数加载失败...");
		}

		//初始化 produce
		new KafkaProducerFactory(producerConf).start();
		super.start();
	}

	@Override
	public void addAppender(Appender<E> newAppender) {
		aai.addAppender(newAppender);
	}


	@Override
	public Iterator<Appender<E>> iteratorForAppenders() {
		return aai.iteratorForAppenders();
	}

	@Override
	public Appender<E> getAppender(String name) {
		return aai.getAppender(name);
	}

	@Override
	public boolean isAttached(Appender<E> appender) {
		return aai.isAttached(appender);
	}

	@Override
	public void detachAndStopAllAppenders() {
		aai.detachAndStopAllAppenders();
	}

	@Override
	public boolean detachAppender(Appender<E> appender) {
		return aai.detachAppender(appender);
	}

	@Override
	public boolean detachAppender(String name) {
		return aai.detachAppender(name);
	}

	@Override
	protected void append(E e) {
		//发送消息到kafka
		exec.execute( new Runnable() {
			@Override
			public void run() {
				KafkaProducerFactory.getKafkaTemplate().sendDefault(e.toString());
			}
		});
	}
	
	

}
