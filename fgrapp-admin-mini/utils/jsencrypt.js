import WxmpRsa from 'wxmp-rsa'

// 密钥对生成 http://web.chacuo.net/netrsakeypair

const publicKey = 'MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAMNRhRdV7BI4MN5buB2Dyj6+dSOEpa6jCiJETtBtwfTuWlerqzdgxvFJHKLrHDscCagHY1X1wXh599LE0fs2nQ8CAwEAAQ=='
// 加密
export function encrypt(txt) {
    // 实例化rsa
  const rsa = new WxmpRsa()
  rsa.setPublicKey(publicKey) // 设置公钥
  return rsa.encryptLong(txt) // 对数据进行加密
}

