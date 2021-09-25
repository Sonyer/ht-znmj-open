<template>
	<!-- 状态栏 -->
	<view class="status"></view>
</template>

<script>
</script>

<style lang="scss">
	.status {
		width: 100%;
		display: flex;
		position: fixed;
		z-index: 10;
		background-color: #ff9900;
		top: 0;
		height: 0;
		/*  #ifdef  MP */
		height: var(--status-bar-height); //覆盖样式
		/*  #endif  */
		/*  #ifdef  APP-PLUS */
		height: var(--status-bar-height); //覆盖样式
		/*  #endif  */
	}
</style>

