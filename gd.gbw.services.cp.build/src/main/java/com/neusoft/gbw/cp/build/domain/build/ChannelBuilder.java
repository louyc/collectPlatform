package com.neusoft.gbw.cp.build.domain.build;

public class ChannelBuilder {

	public static com.neusoft.gbw.cp.conver.v8.protocol.vo.kpi.Channel buildKpiChannel(int bandType, String freq) {
		com.neusoft.gbw.cp.conver.v8.protocol.vo.kpi.Channel channel = new com.neusoft.gbw.cp.conver.v8.protocol.vo.kpi.Channel();
		channel.setBand(bandType+"");
		channel.setFreq(freq);
		return channel;
	}
	
	public static com.neusoft.gbw.cp.conver.v8.protocol.vo.offset.Channel buildOffsetChannel(int bandType, String freq) {
		com.neusoft.gbw.cp.conver.v8.protocol.vo.offset.Channel channel = new com.neusoft.gbw.cp.conver.v8.protocol.vo.offset.Channel();
		channel.setBand(bandType+"");
		channel.setFreq(freq);
		return channel;
	}
	
	public static com.neusoft.gbw.cp.conver.v8.protocol.vo.audio.Channel buildAudioChannel(int bandType, String freq) {
		com.neusoft.gbw.cp.conver.v8.protocol.vo.audio.Channel channel = new com.neusoft.gbw.cp.conver.v8.protocol.vo.audio.Channel();
		channel.setBand(bandType+"");
		channel.setFreq(freq);
		return channel;
	}
	
	public static com.neusoft.gbw.cp.conver.v8.protocol.vo.spectrum.Channel buildSpectrumChannel(int bandType, String freq) {
		com.neusoft.gbw.cp.conver.v8.protocol.vo.spectrum.Channel channel = new com.neusoft.gbw.cp.conver.v8.protocol.vo.spectrum.Channel();
		channel.setBand(bandType+"");
		channel.setFreq(freq);
		return channel;
	}
	
	public static com.neusoft.gbw.cp.conver.v5.protocol.vo.kpi.Channel buildKpiChannelV5(int bandType, String freq) {
		com.neusoft.gbw.cp.conver.v5.protocol.vo.kpi.Channel channel = new com.neusoft.gbw.cp.conver.v5.protocol.vo.kpi.Channel();
		channel.setBand(bandType+"");
		channel.setFreq(freq);
		return channel;
	}
	
	public static com.neusoft.gbw.cp.conver.v5.protocol.vo.offset.Channel buildOffsetChannelV5(int bandType, String freq) {
		com.neusoft.gbw.cp.conver.v5.protocol.vo.offset.Channel channel = new com.neusoft.gbw.cp.conver.v5.protocol.vo.offset.Channel();
		channel.setBand(bandType+"");
		channel.setFreq(freq);
		return channel;
	}
	
	public static com.neusoft.gbw.cp.conver.v5.protocol.vo.audio.Channel buildAudioChannelV5(int bandType, String freq) {
		com.neusoft.gbw.cp.conver.v5.protocol.vo.audio.Channel channel = new com.neusoft.gbw.cp.conver.v5.protocol.vo.audio.Channel();
		channel.setBand(bandType+"");
		channel.setFreq(freq);
		return channel;
	}
	
	public static com.neusoft.gbw.cp.conver.v7.protocol.vo.audio.Channel buildAudioChannelV7(int bandType, String freq) {
		com.neusoft.gbw.cp.conver.v7.protocol.vo.audio.Channel channel = new com.neusoft.gbw.cp.conver.v7.protocol.vo.audio.Channel();
		channel.setBand(bandType+"");
		channel.setFreq(freq);
		return channel;
	}
	
	public static com.neusoft.gbw.cp.conver.v5.protocol.vo.spectrum.Channel buildSpectrumChannelV5(int bandType, String freq) {
		com.neusoft.gbw.cp.conver.v5.protocol.vo.spectrum.Channel channel = new com.neusoft.gbw.cp.conver.v5.protocol.vo.spectrum.Channel();
		channel.setBand(bandType+"");
		channel.setFreq(freq);
		return channel;
	}
	
	public static com.neusoft.gbw.cp.conver.v6.protocol.vo.kpi.Channel buildKpiChannelV6(int bandType, String freq) {
		com.neusoft.gbw.cp.conver.v6.protocol.vo.kpi.Channel channel = new com.neusoft.gbw.cp.conver.v6.protocol.vo.kpi.Channel();
		channel.setBand(bandType+"");
		channel.setFreq(freq);
		return channel;
	}
	
	public static com.neusoft.gbw.cp.conver.v7.protocol.vo.kpi.Channel buildKpiChannelV7(int bandType, String freq) {
		com.neusoft.gbw.cp.conver.v7.protocol.vo.kpi.Channel channel = new com.neusoft.gbw.cp.conver.v7.protocol.vo.kpi.Channel();
		channel.setBand(bandType+"");
		channel.setFreq(freq);
		return channel;
	}
	
	public static com.neusoft.gbw.cp.conver.v6.protocol.vo.offset.Channel buildOffsetChannelV6(int bandType, String freq) {
		com.neusoft.gbw.cp.conver.v6.protocol.vo.offset.Channel channel = new com.neusoft.gbw.cp.conver.v6.protocol.vo.offset.Channel();
		channel.setBand(bandType+"");
		channel.setFreq(freq);
		return channel;
	}
	
	public static com.neusoft.gbw.cp.conver.v7.protocol.vo.offset.Channel buildOffsetChannelV7(int bandType, String freq) {
		com.neusoft.gbw.cp.conver.v7.protocol.vo.offset.Channel channel = new com.neusoft.gbw.cp.conver.v7.protocol.vo.offset.Channel();
		channel.setBand(bandType+"");
		channel.setFreq(freq);
		return channel;
	}
	
	public static com.neusoft.gbw.cp.conver.v6.protocol.vo.audio.Channel buildAudioChannelV6(int bandType, String freq) {
		com.neusoft.gbw.cp.conver.v6.protocol.vo.audio.Channel channel = new com.neusoft.gbw.cp.conver.v6.protocol.vo.audio.Channel();
		channel.setBand(bandType+"");
		channel.setFreq(freq);
		return channel;
	}
	
	public static com.neusoft.gbw.cp.conver.v6.protocol.vo.spectrum.Channel buildSpectrumChannelV6(int bandType, String freq) {
		com.neusoft.gbw.cp.conver.v6.protocol.vo.spectrum.Channel channel = new com.neusoft.gbw.cp.conver.v6.protocol.vo.spectrum.Channel();
		channel.setBand(bandType+"");
		channel.setFreq(freq);
		return channel;
	}
}
