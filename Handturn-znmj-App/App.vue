<script>
	
	
	export default {
		onLaunch: function(options) {
			console.log(options)
			
			console.log('App Launch');
			
		},
		onShow: function() {
			console.log('App Show')
		},
		onHide: function() {
			console.log('App Hide')
		},
		methods: {
			
		}
	}
</script>

<style>
	/*每个页面公共css */
	@import "./static/iconfont/iconfont.css";
	page{
		position: relative;
		background-color: #ffffff;
	}
</style>
