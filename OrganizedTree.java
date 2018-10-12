// FILE:  OrganizedTree.java

import java.util.ArrayList;
import java.util.List;

/**
 *  This program can take an integer as an argument, makes
 *  an array populated with the integers one to the input
 *  integer, and outputs all of the possible sorted binary
 *  trees that can be made with that array.
 *
 *  Integers greater than 8 not fully evaluated for correctness.
 *  Integers 20 and greater will cause an integer overflow
 *  Integers 16 and greater will likely cause an "out of memory" error.
 *
 *  This program can also take a number of strings as an
 *  argument, and output the sorted binary tree the array
 *  of strings would create.
 *
 *  Some sequences of string will create output that look
 *  odd, but are likely still displayed as intended.
 *
 *  @author - Nathan Bradley.
 *  @version - 20.11.2017.
 */

public class OrganizedTree
{

    /**
     *  I could not get javadocs to work with nested classes
     *
     *  A class that holds an integer and two links to other nodes.
     */
    static class Node<Type extends Comparable<Type>>
    {
        private Node<Type> left_link, right_link;
        private int offset;
        private Type data;

        /**
         * Default constructor for a Node, with
         *   the data and links set to null.
         * @param - none
         * @post - 
         *   Initializes a node to null.
         * @exception OutOfMemoryError
         *   Indicates insufficient memory.
         **/ 
        public Node()
        {
            data = null;
            offset = 0;
            left_link = null;
            right_link = null;
        }

        /**
         * Constructor for a Node, with the
         *   data set to the input.
         * @param - Type.
         * @post - 
         *   Initializes a node's data to
         *   the input and it's links to null.
         * @exception OutOfMemoryError
         *   Indicates insufficient memory.
         **/ 
        public Node( Type input_data )
        {
            data = input_data;
            offset = 0;
            left_link = null;
            right_link = null;
        }
                 
   
        /**
        * Returns the data contained by the node.
        * @param - none.
        * @return - Type.
        * @post - class values are unchanged.
        **/
        public Type getData()
        {
            return data;
        }
                 
   
        /**
        * Returns the offset of the node.
        * @param - none.
        * @return - int.
        * @post - class values are unchanged.
        **/
        public int getOffset()
        {
            return offset;
        }
                 
   
        /**
        * Returns the left link of the node.
        * @param - none.
        * @return - Node<Type>.
        * @post - class values are unchanged.
        **/
        public Node<Type> getLeftLink()
        {
            return left_link;
        }
                 
   
        /**
        * Returns the right link of the node.
        * @param - none.
        * @return - Node<Type>.
        * @post - class values are unchanged.
        **/
        public Node<Type> getRightLink()
        {
            return right_link;
        }
                 
   
        /**
        * Adds a new node to a link or a link
        *   of links.
        * @param - Type.
        * @return - none.
        * @post - Adds a new node to a link
        *   or a link of links.
        **/
        public void addLink( Type input_data )
        {
            if( input_data.compareTo( data ) < 0 )
            {
                if( left_link == null )
                {
                    left_link = new Node<Type>( input_data );
                }
                else
                {
                    left_link.addLink( input_data );
                }
            }
            else
            {
                if( right_link == null )
                {
                    right_link = new Node<Type>( input_data );
                }
                else
                {
                    right_link.addLink( input_data );
                }
            }
        }
                 
   
        /**
        * Changes the offset in this node and all of
        *   the desendents of this node in relation to
        *   left justified printing.
        * @param - none.
        * @return - none.
        * @post - all of the offsets in this node
        *   and the desendents of this node are changed.
        **/
        public void configureOffset()
        {
            setOffset(0);
            int furthest_left = find_least_offset();

            if( furthest_left < 1 )
            {
                changeOffset( 1 - furthest_left );
            }
        }
                 
   
        /**
        * This method searches all nodes from this root
        *   and returns the least (likely negative) offset.
        * @param - none.
        * @return - int.
        * @post - class values are unchanged.
        **/
        private int find_least_offset()
        {
            int return_offset;

            if( left_link == null )
            {
                return_offset = offset;
            }
            else
            {
                return_offset = left_link.find_least_offset();
            }

            if( right_link != null )
            {
                int right_least= right_link.find_least_offset();

                if( return_offset > right_least )
                {
                    return_offset = right_least;
                }
            }

            return return_offset;
        }

        private void setOffset( int inherited_offset )
        {
            if( left_link == null )
            {
                offset = inherited_offset;
            }
            else
            {
                left_link.setOffset( inherited_offset - 1 );
                offset = 1 + left_link.getOffset();
            }

            if( right_link != null )
            {
                right_link.setOffset( offset + 1 );

                int spacer = 1;

                if( left_link != null )
                {
                    int new_spacer = 1;

                    while( new_spacer > 0 )
                    { // checks check_decendent_collision() multiple times
                        new_spacer = check_decendent_collision();

                        //System.out.printf( "DEBUG: new_spacer: %d, value: %d\n", new_spacer, data );
                        /*if( spacer > 10 )
                        {
                            System.exit(1);
                        }*/ // debug infinite loop catch

                        if( new_spacer > 0 )
                        { // separates nodes that are too close or over lap
                            left_link.changeOffset( -1 * new_spacer );
                            right_link.changeOffset( new_spacer );
                        }

                        spacer += new_spacer;
                    }
                }
                
                if( offset < right_link.getOffset() - spacer )
                {
                    //System.out.printf( "DEBUG: spacer: %d, value: %d, old offset: %d, calc: %d\n", 
                        //spacer, data, offset, right_link.getOffset() - offset - 2 * spacer + 1  );
                    if( left_link != null )
                    { /* the offset from the right link might be greater
                      than the offset from the left, calling the function
                      for the left again to fix it*/
                        //                       new-offset - old-offset

                        left_link.changeOffset( right_link.getOffset() - offset - spacer );
                    }

                    offset = right_link.getOffset() - spacer;
                }
            }
        }

        private int check_decendent_collision()
        {
            for( int test_depth = depth(); test_depth > 1; --test_depth )
            {
                // the nodes compared must be in the same depth

                //System.out.printf( "DEBUG: test_depth: %d\n", test_depth );
                Node<Type> left_node = left_link.find_furthest_right( test_depth );
                Node<Type> right_node = right_link.find_furthest_left( test_depth );

                if( left_node != null && right_node != null && left_node.getOffset() > right_node.getOffset() - 2 )
                {
                    //System.out.printf( "DEBUG: left_offset: %d\n", left_node.getOffset() );
                    //System.out.printf( "DEBUG: right_offset: %d\n\n", right_node.getOffset() );
                    return 1 + ( left_node.getOffset() - right_node.getOffset() ) / 2;
                }
            }

            return 0;
        }

        private Node<Type> find_furthest_left( int input_depth )
        {
            if( input_depth > 2 )
            {
                Node<Type> return_node = null;

                if( left_link != null )
                {
                    return_node = left_link.find_furthest_left( input_depth - 1 );
                }

                if( return_node == null && right_link != null )
                {
                    return right_link.find_furthest_left( input_depth - 1 );
                }

                return return_node;
            }
            else
            {
                if( left_link == null )
                {
                    return right_link;
                }
                else
                {
                    return left_link;
                }
            }
        }

        private Node<Type> find_furthest_right( int input_depth )
        {
            if( input_depth > 2 )
            {
                Node<Type> return_node = null;

                if( right_link != null )
                {
                    return_node = right_link.find_furthest_right( input_depth - 1 );
                }

                if( return_node == null && left_link != null )
                {
                    return left_link.find_furthest_right( input_depth - 1 );
                }

                return return_node;
            }
            else
            {
                if( right_link == null )
                {
                    return left_link;
                }
                else
                {
                    return right_link;
                }
            }
        }

        private void changeOffset( int input_change )
        {
            offset += input_change;

            if( left_link != null )
            {
                left_link.changeOffset( input_change );
            }

            if( right_link != null )
            {
                right_link.changeOffset( input_change );
            }
        }

        private int depth()
        {
            int return_depth = 1;

            if( left_link != null )
            {
                return_depth += left_link.depth();
            }

            if( right_link != null )
            {
                int right_return = 1 + right_link.depth();

                if( return_depth < right_return )
                {
                    return right_return;
                }
            }

            return return_depth;
        }


        /**
         * This method prints this node and its links'
         *   contents in a generic printed form.
         * This method was designed for debugging purposes.
         * @param - none.
         * @return - none.
         * @post - class values are unchanged.
         **/
        public void print()
        {
            System.out.println( toString() );

            if( left_link != null )
            {
                left_link.print();
            }

            if( right_link != null )
            {
                right_link.print();
            }
        }


        /**
         * This method renders the node's contents
         *   in a generic printed form.
         * This method was designed for debugging purposes.
         * @param - none.
         * @return - String.
         * @post
         *   The node is not altered by this method.
         **/
        public String toString()
        {
            StringBuffer sb = new StringBuffer();

            sb.append( String.format( "Node: %d, Offset: %d", data, offset ) );

            if( left_link != null )
            {
                sb.append( ", has left link" );
            }

            if( right_link != null )
            {
                sb.append( ", has right link" );
            }

            return sb.toString();
        }
    }

    /**
    * A class that holds a binary search tree on integer nodes.
    *  Primary use is for prints the tree in a ascii fashion.
    */
    static class Tree<Type extends Comparable<Type>>
    {
        private Node<Type> root;
        private int longest_width;


        /**
         * Default constructor for a tree.
         * @param - none.
         * @post - an empty tree is allocated.
         * @exception OutOfMemoryError
         *   Indicates insufficient memory.
         **/ 
        public Tree()
        {
            root = null;
            longest_width = 0;
        }


        /**
         * Constructs a tree using the 
         *   specified preexisting array.
         * @param - int array.
         * @post -
         *   a tree constructed from the supplied array.
         * @exception OutOfMemoryError
         *   Indicates insufficient memory for the tree.
         **/
        public Tree( Type[] input_array )
        {
            root = new Node<Type>( input_array[0] );
            longest_width = 0;

            for( int index = 1; index < input_array.length; ++index )
            {
                root.addLink( input_array[index] );

                if( longest_width < input_array[index].toString().length() )
                {
                    longest_width = input_array[index].toString().length();
                }
            }
            
            // offset CAN_NOT be determined during node creation
            root.configureOffset();
        }

        private void depth_data( Node<Type> local_root, int input_depth, List<List<Node<Type>>> value_list )
        {
            if( local_root != null )
            {
                if( value_list.size() <= input_depth )
                {
                    value_list.add( new ArrayList<Node<Type>>() );
                }

                value_list.get( input_depth ).add( local_root );

                depth_data( local_root.getLeftLink(), input_depth + 1, value_list );
                depth_data( local_root.getRightLink(), input_depth + 1, value_list );
            }
        }


        /**
         * This method renders the tree's contents into an ascii graphic form.
         * @param - none.
         * @return - String.
         * @post - The tree is not altered by this method.
         **/
        public String toString()
        {
            return toString( false );
        }


        /**
         * This method renders the tree's contents into an ascii graphic form.
         *   The graphical link can be set to be wider
         * @param - int.
         * @return - String.
         * @post - The tree is not altered by this method.
         **/
        public String toString( boolean wide_link )
        {
            StringBuffer sb = new StringBuffer();

            List<List<Node<Type>>> depth_array = new ArrayList<List<Node<Type>>>();
            depth_data( root, 0, depth_array );

            for(List<Node<Type>> step_array: depth_array )
            {
                int previus_offset = 0;

                for( Node<Type> current_node: step_array )
                {
                    if( current_node.getOffset() - previus_offset > 1 )
                    {
                        sb.append( String.format( "%" + ( ( 2 + longest_width ) * 
                            ( current_node.getOffset() - previus_offset - 1) ) + "s", "" ) );
                    }

                    sb.append("(");

                    int spacing_difference = longest_width - current_node.getData().toString().length();

                    sb.append( String.format( "%" + ( longest_width - spacing_difference / 2 ) + "s",
                        current_node.getData().toString() ) );

                    if( spacing_difference > 1 )
                    {
                        sb.append( String.format( "%" + ( spacing_difference / 2 )+ "s", "" ) );
                    }

                    sb.append(")");

                    previus_offset = current_node.getOffset();
                }

                if( step_array != depth_array.get( depth_array.size() - 1 ) )
                { // if the step_array isnt the last list in depth_array    print the links
                    previus_offset = 0;
                    sb.append( "\n" );

                    if( wide_link )
                    {
                        sb.append( " " );
                    }

                    for( Node<Type> current_node: step_array )
                    {
                        StringBuffer link_sb = new StringBuffer();

                        if( current_node.getLeftLink() == null )
                        {
                            if( wide_link )
                            {
                                if( current_node.getOffset() != 1 )
                                {
                                    link_sb.append( "  " );
                                }
                            }
                            else
                            {
                                link_sb.append( " " );
                            }
                        }
                        else
                        {
                            link_sb.append( "/" );

                            if( wide_link )
                            {
                                link_sb.append( "/" );
                            }
                        }

                        link_sb.append( String.format( "%" + longest_width + "s", "" ) );

                        if( current_node.getRightLink() == null )
                        {
                            link_sb.append( " " );

                            if( wide_link )
                            {
                                link_sb.append( " " );
                            }
                        }
                        else
                        {
                            link_sb.append( "\\" );

                            if( wide_link )
                            {
                                link_sb.append( "\\" );
                            }
                        }

                        sb.append( String.format( "%" + ( ( 2 + longest_width ) * ( current_node.getOffset() - previus_offset ) ) + "s",
                            link_sb.toString() ) );
                        previus_offset = current_node.getOffset();
                    }

                    sb.append( "\n" );
                }
            }

            return sb.toString();
        }


        /**
         * This method prints the tree's contents in a generic printed form.
         * This method was designed for debugging purposes.
         * @param - none.
         * @return - none.
         * @post - The tree is not altered by this method.
         **/
        public void debug_print()
        {
            if( root != null )
            {
                root.print();
            }
            else
            {
                System.out.print( "Empty tree" );
            }
        }
    }

    public static Integer[] int_to_integer_arrays( int[] input_array )
    {
        Integer[] return_array = new Integer[input_array.length];

        for( int index = 0; index < input_array.length; ++index )
        {
            return_array[index] = Integer.valueOf( input_array[index] );
        }

        return return_array;
    }


    /**
     * This method recursively returns the number of ways a binary
     *  tree of input_count size can be constructed.
     * @param - int.
     * @return - int.
     * @post - a (probably large) integer is allocated and calculated.
     **/
    public static int permutate_count( int input_count )
    {
        //p(3) == p(0)*p(2) + p(1)*p(1) + p(2)*p(0) == 2+1+2 == 5
        //p(4) == p(0)*p(3) + p(1)*p(2) + p(2)*p(1) + p(3)*p(0) == 5+2+2+5 == 14

        if( input_count > 1 )
        {
            int return_count = 0;

            for( int iteration = 0; iteration < input_count; ++iteration )
            {
                return_count += permutate_count( iteration ) * permutate_count( input_count - iteration - 1 );
            }

            return return_count;
        }
        else
        {
            return 1;
        }
    }


    /**
     * This method recursively returns every possible array the input_array
     *  can be arranged to construct a binary tree.
     * @param - int array.
     * @return - two dimentional int array.
     * @post - a (probably lengthly) array is alocated an populated.
     **/
    public static Integer[][] permutate( Integer[] input_array )
    {
        if( input_array.length > 1 )
        {
            Integer[][] permutations = new Integer[ permutate_count( input_array.length ) ][ input_array.length ];
            int master_index = 0;

            for( int index = 0; index < input_array.length; ++index )
            {
                Integer[] subleft = new Integer[ index ];
                Integer[] subright = new Integer[ input_array.length - index - 1 ];

                int left_index = 0;
                int right_index = 0;

                for( Integer array_element: input_array )
                {
                    if( array_element < input_array[ index ] )
                    {
                        subleft[ left_index++ ] = array_element;
                    }

                    if( array_element > input_array[ index ] )
                    {
                        subright[ right_index++ ] = array_element;
                    }
                }

                for( Integer[] permutated_left: permutate( subleft ) )
                {
                    for( Integer[] permutated_right: permutate( subright ) )
                    {
                        permutations[ master_index ][0] = input_array[ index ];
                        System.arraycopy(permutated_left, 0, permutations[ master_index ], 1, permutated_left.length);
                        System.arraycopy(permutated_right, 0, permutations[ master_index ], 1 + permutated_left.length, permutated_right.length);

                        ++master_index;
                    }
                }
            }

            return permutations;
        }
        else
        {
            return new Integer[][] {input_array};
        }
    }


    /**
     * This method constructs an array with the values 1 to input_size
     *  and prints all the binary trees that could be made from any
     *  arrangement of that array.
     * Arguments above a certain value will not be printed as a tree,
     *  instead will be printed as a sequence of numbers.
     * @param - int.
     * @return - none.
     * @post - 
     *  This method does not change any of the rest of the program.
     **/
    public static void print_all_trees( int input_size )
    {
        if( input_size > 19 )
        {
            System.out.printf( "Input Error: %d will cause an interger over flow\n", input_size );
            return;
        }

        if( input_size > 15 )
        {
            System.out.printf( "Binary search tree count = %,d\n", permutate_count( input_size ) );
            System.out.printf( "Input Warning: %d will likely cause an \"out of memory\" error\n", input_size );
        }

        if( input_size > 0 )
        {
            Integer[] array_set = new Integer[ input_size ];

            for( int value = 0; value < input_size; )
            {
                array_set[ value ] = ++value;
            }

            int BTScount = 0;

            for( Integer[] sequence: permutate( array_set ) )
            {
                System.out.printf( "Tree number %,d: ", ++BTScount );
                print_this_tree( sequence );
            }

            System.out.printf( "Binary search tree count = %,d\n", BTScount );
        }
        else
        {
            System.out.printf( "Input Error: %d is not an acceptible input.\n", input_size );
        }
    }


    /**
     * This method renders an array's contents into an ascii graphic form.
     * @param - int array.
     * @return - String.
     * @post - a string describing the input_array is constructed.
     **/
    public static <Type extends Comparable<Type>> String array_toString( Type[] input_array )
    {
        StringBuffer sb = new StringBuffer();

        sb.append( "{" );

        if( input_array.length > 0 )
        {
            sb.append( String.format( "%s", input_array[ 0 ].toString() ) );
        }
        else
        {
            sb.append( " " );
        }

        for( int index = 1; index < input_array.length; ++index )
        {
            sb.append( String.format( ", %s", input_array[ index ].toString() ) );
        }

        sb.append( "}" );

        return sb.toString();
    }


    /**
     * This method constructs and prints a
     *  binary tree using the input array.
     * @param - int array.
     * @return - none.
     * @post
     *  This method does not change any of the rest of the program.
     **/
    public static <Type extends Comparable<Type>> void print_this_tree( Type[] input_sequence )
    {
        Tree sequence_tree = new Tree<Type>( input_sequence );
        System.out.printf( "%s\n", array_toString( input_sequence ) );
        System.out.printf( "%s\n\n", sequence_tree.toString() ); // separated for debugging purposes
        //sequence_tree.debug_print(); // statement for debugging purposes
    }

    public static void main( String[] args )
    {
        if( args.length == 1 && args[0].chars().allMatch( Character::isDigit ) )
        {
            print_all_trees( Integer.parseInt( args[0] ) );
        }
        else if( args.length > 0 )
        {
            print_this_tree( args ); // tree of strings
        }
        else // default
        {
            print_this_tree( new Integer[] {8, 4, 2, 1, 3, 6, 5, 7, 15, 12, 10, 11, 9, 13, 14, 16, 17, 19, 18} );
            print_this_tree( new Integer[] {3, 2, 1, 6, 4, 5, 8, 7} );
            print_this_tree( new String[] {"W", "e", " ", "L", "o", "v", "e", " ", "C", "S"} );
            print_this_tree( new Integer[] {10, 1, 2, 3, 4, 5, 6, 7, 8, 9, 19, 18, 17, 16, 15, 14, 13, 12, 11} ); // deep V
            print_this_tree( new Integer[] {16, 8, 4, 2, 1, 3, 6, 5, 7, 12, 10, 9, 11, 14, 13, 15, 24, 20, 18, 17, 19, 22, 21, 23, 28, 26, 25, 27, 30, 29, 31} ); // full binary tree 31
            print_all_trees( 4 );
        }

        System.exit(0);
    }
}
