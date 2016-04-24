package jc.component;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.List;

/**
 * Created by 金成 on 2016/4/11.
 * BloomFilter过滤器
 */
public class BloomFilter<T> implements Serializable {

    private static final int DEFAULT_SIZE = 100000;
    private static final double DEFAULT_FALSEPOSITIVERATE = 0.01;

    private int size;
    private int n;
    private double falsePositiveRate;
    private int count;

    private List<BloomFilterItem<T>> bloomFilters;
    private int index;

    public BloomFilter() {
        this(DEFAULT_SIZE, DEFAULT_FALSEPOSITIVERATE);
    }

    public BloomFilter(int n) {
        this(n, DEFAULT_FALSEPOSITIVERATE);
    }

    public BloomFilter(double falsePositiveRate) {
        this(DEFAULT_SIZE, falsePositiveRate);
    }

    public BloomFilter (int n, double falsePositiveRate){

        this.n = n;
        this.size = n;
        this.falsePositiveRate = falsePositiveRate;
        this.count = 0;
        this.index = 0;

        bloomFilters = new ArrayList<>();
        BloomFilterItem<T> bloomFilterItem = new BloomFilterItem<>(this.n, this.falsePositiveRate);
        bloomFilters.add(bloomFilterItem);
    }

    public void add(T key){

        if (count < n){
            BloomFilterItem<T> bloomFilterItem = bloomFilters.get(index);
            bloomFilterItem.add(key);
        }else {
            size *= 2;
            n += size;
            index++;
            BloomFilterItem<T> bloomFilterItem = new BloomFilterItem<>(size, falsePositiveRate);
            bloomFilterItem.add(key);
            bloomFilters.add(bloomFilterItem);
        }
        count++;
    }

    public boolean contains(T key){

        for (BloomFilterItem bloomFilterItem : bloomFilters){
            if (bloomFilterItem.contains(key)){
                return true;
            }
        }

        return false;
    }



    private class BloomFilterItem<T> implements Serializable{

        private double falsePositiveRate; // 错误率
        private int n; // 其中key的数量
        private int m; // bit数组的宽度，通过key数量和错误率计算得到
        private int k; // hash函数的个数,通过key数量和错误率计算得到
        private BitSet bitSet; // bit数组

        public BloomFilterItem(){
            this(DEFAULT_SIZE, DEFAULT_FALSEPOSITIVERATE);
        }

        public BloomFilterItem(int n){
            this(n, DEFAULT_FALSEPOSITIVERATE);
        }

        public BloomFilterItem(double falsePositiveRate){
            this(DEFAULT_SIZE, falsePositiveRate);
        }

        public BloomFilterItem(int n, double falsePositiveRate) {
            this.n = n;
            this.falsePositiveRate = falsePositiveRate;

            m = (int) Math.ceil(1.44*((double) n)*Math.log(1/falsePositiveRate));
            k = (int) Math.ceil(Math.log(1/falsePositiveRate)/Math.log(2));
            bitSet = new BitSet(m);
        }

        public void add(T key){
            byte[] bytes = toBytes(key);
            long[] hashes = hashFunctions(bytes, k);

            for (long hash : hashes){
                int location = (int)(hash % m);
                bitSet.set(location);
            }
        }

        public boolean contains(T key){
            byte[] bytes = toBytes(key);
            long[] hashes = hashFunctions(bytes, k);

            for (long hash : hashes){
                int location = (int)(hash % m);
                if (!bitSet.get(location)){
                    return false;
                }
            }
            return true;
        }

    }

    private static <T> byte[] toBytes(T key){

        byte[] bytes = null;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        try {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(key);
            objectOutputStream.flush();
            bytes = byteArrayOutputStream.toByteArray ();
            objectOutputStream.close();
            byteArrayOutputStream.close();
        } catch (NotSerializableException e){
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bytes;
    }

    private static long[] hashFunctions(byte[] bytes, int k){

        byte[] hash = hash(bytes);
        long[] hashes = new long[k];

        long hash1 = 0;
        for (int i = 0 ; i < 4 ; i++){
            hash1 <<= 8;
            hash1 |= hash[i] & 0xff;
        }

        long hash2 = 0;
        for (int i = 4 ; i < 8 ; i++){
            hash2 <<= 8;
            hash2 |= hash[i] & 0xff;
        }

        for (int i = 1 ; i <= k; i++){
            hashes[i - 1] = hash1 + i*hash2;
        }

        return hashes;
    }

    /**
     * 后期考虑使用md5的实现。
     * */
    private static byte[] hash(byte[] bytes){

        int[] S = new int[]{0xaa, 0xb5, 0xc5, 0xd5, 0xe5, 0xf5, 0x15, 0x51};

        byte[] result = new byte[8];
        int fill = 8- bytes.length % 8;
        byte[] bytesAligned = Arrays.copyOf(bytes, bytes.length + fill);

        for (int i = 0; i < bytesAligned.length / 8 ; i++){
            for (int j = 0 ; j < 8 ; j++){
                result[j] ^= (bytesAligned[i*8 + j] ^ result[(j+1) % 8] ^ S[j]);
            }
        }

        return result;
    }


}
